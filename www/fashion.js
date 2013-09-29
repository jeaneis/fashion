YUI().use("io-xdr", "json-parse", "json-stringify", "node", 
          "overlay", "anim", "plugin", "gallery-overlay-transition",
          "event-hover", "event-mouseenter", "transition",
          function(Y) {

  //
  //  GLOBALS
  //

  var numSuggestions = 4;
  var queryID = 0;
  var clothingSlots = ['top', 'bottom', 'shoe'];

  //
  //  AJAX CALLS
  //
  var getItem = function(id, callback) {
    Y.io( "http://localhost:4242/lookup_item?id=" + encodeURIComponent(id), {
      method: "GET", on: { success: function(id, o, a){ callback(Y.JSON.parse(o.responseText)); } }
    });
  }
  
  var getRandItem = function(clothingType, callback) {
  	Y.io( "http://localhost:4242/random_item?type=" + encodeURIComponent(clothingType),
  	{
  		method: "GET",
  		on: { success: function(id, o, a) { Y.log(o); callback(Y.JSON.parse(o.responseText)); } }
  	});
  }

  var getRecommendations = function(clothingType, ids, callback) {
    var query = {
      type: clothingType,
      input:((ids instanceof Array) ? ids : [ids]),
      start:0,
      count:numSuggestions
    };
    Y.io( "http://localhost:4242/recommend?data=" + encodeURIComponent(Y.JSON.stringify(query)),
      {method: "GET", on: { success: function(id, o, a){
        Y.log(o.responseText);
        callback(Y.JSON.parse(o.responseText)); } }
      });
  }
  
  //
  //  UI HOOKS
  //

  function selectAlternative(e) {
    var animationDuration = 0.25;
    // Paths
    Y.log(Y.one('.selectedRegion'));
    var srcImg = Y.one('.selectedRegion');
    var srcImgPath = srcImg.get('src');
    var targetImgPath = e.target.get('src');
    // (without animation)
//    e.target.set('src', srcImgPath);
//    srcImg.set('src', targetImgPath);
    // Animation (src)
    var fadeOutSrc = new Y.Anim({
            node: '#' + e.target.get('id'),
            from: { opacity: 1 },
            to: { opacity: 0 },
            duration: animationDuration,
        })
    var fadeInSrc = new Y.Anim({
            node: '#' + e.target.get('id'),
            from: { opacity: 0 },
            to: { opacity: 1 },
            duration: animationDuration,
        })
    fadeOutSrc.on('end', function() {
      e.target.set('src', srcImgPath);
      fadeInSrc.run();
    });
    // Animation (targetr)
    var fadeOutTarget = new Y.Anim({
            node: '#' + srcImg.get('id'),
            from: { opacity: 1 },
            to: { opacity: 0 },
            duration: animationDuration,
        });
    var fadeInTarget = new Y.Anim({
            node: '#' + srcImg.get('id'),
            from: { opacity: 0 },
            to: { opacity: 1 },
            duration: animationDuration,
        });
    fadeOutTarget.on('end', function() {
      srcImg.set('src', targetImgPath);
      fadeInTarget.run();
    });
    // Execute
    fadeOutSrc.run();
    fadeOutTarget.run();
  }
  
  Y.one('#top').on('click', function () { openCloset(Y.one('#top')); });
  Y.one('#bottom').on('click', function () { openCloset(Y.one('#bottom')); });
  Y.one('#shoe').on('click', function () { openCloset(Y.one('#shoe')); });

  //
  //  POSITIONING
  //

  function render() {
    // Helpers
    function setWidth(elem, canvasWidth) {
      if (parseInt(elem.getStyle('width')) > canvasWidth / 2) { elem.set('width', canvasWidth / 2); }
    }

    // Collect variables
    var canvas = Y.one('#canvas');
    var canvasXY = canvas.getXY();                                       var canvasWidth = parseInt(canvas.getStyle('width'));
    var imgTop = Y.one('#top');       setWidth(imgTop, canvasWidth);     var topWidth    = parseInt(imgTop.getStyle('width'));    
    var imgBottom = Y.one('#bottom'); setWidth(imgBottom, canvasWidth);  var bottomWidth = parseInt(imgBottom.getStyle('width')); 
    var imgShoe = Y.one('#shoe');     setWidth(imgShoe, canvasWidth);    var shoeWidth   = parseInt(imgShoe.getStyle('width'));   
    var centerX = canvasXY[0] + (canvasWidth / 2);
   
    // Position
    imgTop.setXY([centerX - (topWidth / 2), 
                  canvasXY[1]]);
    imgBottom.setXY([centerX - (bottomWidth / 2),
                    imgTop.getXY()[1] + imgTop.getStyle('height')]);
    imgShoe.setXY([centerX - (shoeWidth / 2),
                   imgBottom.getXY()[1] + imgBottom.getStyle('height')]);
  }
    
    
  var overlay = new Y.Overlay({
      srcNode: "#closet",
  });
  overlay.render();
  overlay.hide();

  function openCloset(parentElem) {
    // Ensure we're not clobbering ourselves
    queryID += 1;
    var currentID = queryID;
    // Manage selected node
    Y.all('.selectedRegion').removeClass('selectedRegion');
    parentElem.addClass('selectedRegion');
    Y.log(Y.one('.selectedRegion'));
    
    // Get Variables
    var parentXY = parentElem.getXY();
    var parentWidth = parseInt(parentElem.getStyle('width'));
    var parentHeight = parseInt(parentElem.getStyle('height'));

    // Initialize overlay
    var closet = Y.one("#closet")
    closet.setHTML('');
    closet.append('<img id="loading" src="images/loading.gif" ' +
        'width= "' + parentWidth + '" ' +
        'style="opacity: 0"/>');

    // Find ids for the rest of the wardrobe
    var id = [];
    var idToReplace = getIdFromElem(parentElem);
    for (i = 0; i < clothingSlots.length; ++i) {
      var candidateId = getIdFromElem(Y.one('#' + clothingSlots[i]));
      if (candidateId != idToReplace) { id.push(candidateId); }
    }
    // Get Suggestions (while we animate)
    Y.log("querying " + id);
    getRecommendations(parentElem.get('id'), id, function(recs) {
      if (currentID == queryID) {  // make sure the request isn't stale...
        // Get rid of loading icon
        Y.one('#loading').remove();
        for (i = 0; i < recs.length; ++i) {
          // Add raw element
          var leftPadding = 25;
          if ((i - (recs.length / 2)) < 0.25) {
            Y.log("HERE");
            Y.log(leftPadding);
            Y.log(parentWidth);
            leftPadding = parentWidth + 50;
          }
          Y.one("#closet").append(
            '<img id="rec_' + i + '" src="/img_clothes/' + recs[i].id + '_in_pp.jpg" ' +
            'width="' + parentWidth + '" ' +
            'style="padding-left: ' + leftPadding + '" ' +
            '/>');
          // Register actions for elements
          var alternativeImage = Y.one('#rec_' + i);
          alternativeImage.setStyle('opacity', '0.75');
          alternativeImage.on('click', selectAlternative);
          alternativeImage.on('mouseenter', function(e) { e.target.setStyle('opacity', '1'); });
          alternativeImage.on('mouseleave', function(e) { e.target.setStyle('opacity', '0.75'); });
        }
        // Add a close button
//        Y.one("#closet").append('<img id="close" src="images/close.png"/>');
//        Y.one('#close').on('click', function() { alert("closing window (but not)"); });
      }
    });
    
    // Render Overlay
    overlay.set('height', parentHeight);
    overlay.set('width', ((parentWidth + 25) * 5 + 25) );
    var fadeOut = new Y.Anim({
            node: '#closet',
            from: { opacity: 1 },
            to: { opacity: 0 },
            duration: 0.05,
        })
    var fadeIn = new Y.Anim({
            node: '#closet',
            from: { opacity: 0 },
            to: { opacity: 1 },
            duration: 0.1,
        })
    fadeIn.on('end', function() { 
      overlay.show();
      // render original image
      new Y.Overlay({
          srcNode: "#loading",
          xy: parentElem.getXY(),
      }).render();
      var loadingIcon = Y.one('#loading');
      if (loadingIcon != null) { loadingIcon.setStyle('opacity', '1'); }
    });
    fadeOut.on('end', function() {
      overlay.move([parentXY[0] + parentWidth / 2 - parseInt(overlay.get('width')) / 2, parentXY[1]]);
      fadeIn.run();
    });
    if (Y.one('#closet').getStyle('opacity') == 0) {
      overlay.move([parentXY[0] + parentWidth / 2 - parseInt(overlay.get('width')) / 2, parentXY[1]]);
      fadeIn.run();
    } else {
      fadeOut.run();
    }
  }

  //
  //  Utils
  //
  function getIdFromElem(parentElem) {
    return parseInt(/img_clothes\/(\d+)[^\d]+/.exec(parentElem.get('src'))[1], 10);
  }
  
  //
  //  INITIALIZATION
  //
  
  getRandItem('Top', function(obj) {
    Y.one('#top').set('src', "/img_clothes/" + obj.id + "_in_pp.jpg");
  });
  getRandItem('Shoe', function(obj) {
    Y.one('#shoe').set('src', "/img_clothes/" + obj.id + "_in_pp.jpg");
  });
  render();
  
});
  
