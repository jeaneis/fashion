/**
 * Adobe Edge: symbol definitions
 */
(function($, Edge, compId){
//images folder
var im='images/';

var fonts = {};


var resources = [
];
var symbols = {
"stage": {
   version: "2.0.1",
   minimumCompatibleVersion: "2.0.0",
   build: "2.0.1.268",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: false,
   resizeInstances: false,
   content: {
         dom: [
         {
            id:'SHOES',
            type:'rect',
            rect:['107px','481px','114px','114px','auto','auto'],
            borderRadius:["0px","0px","10px","10px"],
            fill:["rgba(255,255,255,1.00)"],
            stroke:[5,"rgba(0,0,0,1)","none"],
            boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
         },
         {
            id:'LOGO',
            type:'rect',
            rect:['686px','181px','241px','240px','auto','auto'],
            borderRadius:["13px 13px","13px 13px","13px 13px","13px 13px"],
            fill:["rgba(255,255,255,1.00)"],
            stroke:[5,"rgb(0, 0, 0)","solid"],
            boxShadow:["",0,0,6,0,"rgba(0,0,0,0)"]
         },
         {
            id:'HAT',
            type:'rect',
            rect:['107px','13px','114px','114px','auto','auto'],
            borderRadius:["10px","10px","0px","0px"],
            fill:["rgba(255,255,255,1.00)"],
            stroke:[5,"rgba(0,0,0,1)","none"],
            boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
         },
         {
            id:'TOP_GRP',
            type:'group',
            rect:['65px','131px','198','114','auto','auto'],
            c:[
            {
               id:'SHIRT',
               type:'rect',
               rect:['42px','0px','114px','171px','auto','auto'],
               borderRadius:["1px 1px","1px 1px","1px 1px","1px 1px"],
               fill:["rgba(255,255,255,1.00)"],
               stroke:[5,"rgba(0,0,0,1)","none"],
               boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
            },
            {
               id:'TOP_RT',
               type:'group',
               rect:['161px','62','43','47','auto','auto'],
               c:[
               {
                  id:'TOP_RT_SQR',
                  type:'rect',
                  rect:['0px','5px','42px','42px','auto','auto'],
                  borderRadius:["10px","10px","10px","10px"],
                  fill:["rgba(0,0,0,1.00)"],
                  stroke:[0,"rgb(0, 0, 0)","none"],
                  boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
               },
               {
                  id:'TOP_RT_ARW',
                  type:'text',
                  rect:['1px','0px','42px','42px','auto','auto'],
                  text:"&gt;",
                  align:"center",
                  font:['Arial Black, Gadget, sans-serif',35,"rgba(255,255,255,1.00)","normal","none",""],
                  textShadow:["rgba(0,0,0,0.648438)",0,0,0],
                  filter:[0,0,1,1,0,0,0,0,"rgba(0,0,0,0)",0,0,0]
               }]
            },
            {
               id:'TOP_LT',
               type:'group',
               rect:['-6px','62','43','47','auto','auto'],
               c:[
               {
                  id:'TOP_LT_SQR',
                  type:'rect',
                  rect:['1px','5px','42px','42px','auto','auto'],
                  borderRadius:["10px","10px","10px","10px"],
                  fill:["rgba(0,0,0,1.00)"],
                  stroke:[0,"rgb(0, 0, 0)","none"],
                  boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
               },
               {
                  id:'TOP_LT_ARW',
                  type:'text',
                  rect:['0px','0px','42px','42px','auto','auto'],
                  text:"&lt;",
                  align:"center",
                  font:['Arial Black, Gadget, sans-serif',35,"rgba(255,255,255,1.00)","normal","none",""],
                  textShadow:["rgba(0,0,0,0.648438)",0,0,0]
               }]
            },
            {
               id:'TOP_IMG',
               type:'image',
               rect:['42px','0px','114px','171px','auto','auto'],
               fill:["rgba(0,0,0,0)",im+"389883_in_pp.jpg",'0px','0px']
            },
            {
               id:'TOP_OPN_RCK_BG',
               type:'rect',
               rect:['220px','67px','141px','42px','auto','auto'],
               borderRadius:["10px","10px","10px","10px"],
               fill:["rgba(255,255,255,1.00)"],
               stroke:[0,"rgb(0, 0, 0)","none"],
               boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
            },
            {
               id:'TOP_OPN_RCK_TXT',
               type:'text',
               rect:['234px','75px','114px','auto','auto','auto'],
               text:"OPEN RACK",
               align:"center",
               font:['\'Arial Black\', Gadget, sans-serif',17,"rgba(255,210,248,1)","normal","none","normal"]
            }]
         },
         {
            id:'BOT_GRP',
            type:'group',
            rect:['65px','306px','198','114','auto','auto'],
            c:[
            {
               id:'BOT_RT',
               type:'group',
               rect:['161px','63px','43','47','auto','auto'],
               c:[
               {
                  id:'BOT_RT_SQR',
                  type:'rect',
                  rect:['0px','5px','42px','42px','auto','auto'],
                  borderRadius:["10px","10px","10px","10px"],
                  fill:["rgba(0,0,0,1.00)"],
                  stroke:[0,"rgb(0, 0, 0)","none"],
                  boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
               },
               {
                  id:'BOT_RT_ARW',
                  type:'text',
                  rect:['1px','0px','42px','42px','auto','auto'],
                  text:"&gt;",
                  align:"center",
                  font:['Arial Black, Gadget, sans-serif',35,"rgba(255,255,255,1.00)","normal","none",""],
                  textShadow:["rgba(0,0,0,0.648438)",0,0,0],
                  filter:[0,0,1,1,0,0,0,0,"rgba(0,0,0,0)",0,0,0]
               }]
            },
            {
               id:'BOT_LT',
               type:'group',
               rect:['-6px','62','43','47','auto','auto'],
               c:[
               {
                  id:'BOT_LT_ARW',
                  type:'rect',
                  rect:['1px','5px','42px','42px','auto','auto'],
                  borderRadius:["10px","10px","10px","10px"],
                  fill:["rgba(0,0,0,1.00)"],
                  stroke:[0,"rgb(0, 0, 0)","none"],
                  boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
               },
               {
                  id:'BOT_LT_SQR',
                  type:'text',
                  rect:['0px','0px','42px','42px','auto','auto'],
                  text:"&lt;",
                  align:"center",
                  font:['Arial Black, Gadget, sans-serif',35,"rgba(255,255,255,1.00)","normal","none",""],
                  textShadow:["rgba(0,0,0,0.648438)",0,0,0]
               }]
            },
            {
               id:'BOT_BG',
               type:'rect',
               rect:['42px','0px','114px','171px','auto','auto'],
               borderRadius:["1px 1px","1px 1px","1px 1px","1px 1px"],
               fill:["rgba(255,255,255,1.00)"],
               stroke:[5,"rgba(0,0,0,1)","none"],
               boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
            },
            {
               id:'BOT_IMG',
               type:'image',
               rect:['42px','0px','114px','171px','auto','auto'],
               fill:["rgba(0,0,0,0)",im+"389883_in_pp.jpg",'0px','0px']
            }]
         },
         {
            id:'BOT_OPN_RCK_SQR',
            type:'rect',
            rect:['285px','373px','141px','42px','auto','auto'],
            borderRadius:["10px","10px","10px","10px"],
            fill:["rgba(255,255,255,1.00)"],
            stroke:[0,"rgb(0, 0, 0)","none"],
            boxShadow:["",0,0,6,0,"rgba(0,0,0,0.648438)"]
         },
         {
            id:'BOT_OPN_RCK_TXT',
            type:'text',
            rect:['299px','381px','114px','auto','auto','auto'],
            text:"OPEN RACK",
            align:"center",
            font:['\'Arial Black\', Gadget, sans-serif',17,"rgba(255,210,248,1)","normal","none","normal"]
         }],
         symbolInstances: [

         ]
      },
   states: {
      "Base State": {
         "${_TextCopy14}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '161px'],
            ["style", "font-size", '35px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-weight", 'normal']
         ],
         "${_TextCopy9}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '-5px'],
            ["style", "width", '42px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-size", '35px'],
            ["style", "font-weight", 'normal']
         ],
         "${_Text2Copy}": [
            ["style", "top", '239px'],
            ["style", "font-size", '17px'],
            ["style", "left", '299px'],
            ["style", "width", '114px']
         ],
         "${_BOT_RT_ARW}": [
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-weight", 'normal'],
            ["style", "left", '1px'],
            ["style", "font-size", '35px'],
            ["style", "top", '0px'],
            ["style", "text-align", 'center'],
            ["subproperty", "textShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["subproperty", "filter.drop-shadow.blur", '0px']
         ],
         "${_BOT_GRP}": [
            ["style", "top", '306px'],
            ["style", "left", '65px']
         ],
         "${_SHIRT}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["style", "border-top-left-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "border-bottom-right-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "border-style", 'none'],
            ["style", "border-width", '5px'],
            ["style", "top", '0px'],
            ["style", "border-bottom-left-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["subproperty", "boxShadow.spread", '0px'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["style", "height", '171px'],
            ["subproperty", "boxShadow.offsetV", '0px'],
            ["style", "left", '42px'],
            ["style", "border-top-right-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ]
         ],
         "${_TOP_RT_SQR}": [
            ["style", "top", '5px'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '0px'],
            ["color", "background-color", 'rgba(0,0,0,1.00)']
         ],
         "${_TextCopy16}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '161px'],
            ["style", "font-size", '35px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-weight", 'normal']
         ],
         "${_BOT_LT_ARW}": [
            ["style", "top", '5px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["color", "background-color", 'rgba(0,0,0,1.00)'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '1px'],
            ["subproperty", "boxShadow.blur", '6px']
         ],
         "${_TextCopy15}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '-5px'],
            ["style", "width", '42px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-size", '35px'],
            ["style", "font-weight", 'normal']
         ],
         "${_BOT_IMG}": [
            ["style", "top", '0px'],
            ["style", "height", '171px'],
            ["style", "left", '42px'],
            ["style", "width", '114px']
         ],
         "${_TextCopy5}": [
            ["style", "top", '214px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "left", '27px'],
            ["style", "width", '42px']
         ],
         "${_TextCopy12}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '161px'],
            ["style", "font-size", '35px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-weight", 'normal']
         ],
         "${_LOGO}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["style", "border-top-left-radius", [13,13], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["subproperty", "boxShadow.blur", '6px'],
            ["style", "border-bottom-right-radius", [13,13], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "border-style", 'solid'],
            ["style", "left", '686px'],
            ["style", "width", '241px'],
            ["style", "top", '181px'],
            ["style", "border-bottom-left-radius", [13,13], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "border-width", '5px'],
            ["style", "height", '240px'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "border-top-right-radius", [13,13], {valueTemplate:'@@0@@px @@1@@px'} ]
         ],
         "${_TOP_GRP}": [
            ["style", "top", '131px'],
            ["style", "left", '65px']
         ],
         "${_TextCopy11}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '-5px'],
            ["style", "width", '42px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-size", '35px'],
            ["style", "font-weight", 'normal']
         ],
         "${_BOT_BG}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["style", "border-top-left-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "border-bottom-right-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "border-style", 'none'],
            ["style", "border-width", '5px'],
            ["style", "top", '0px'],
            ["style", "border-bottom-left-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "border-top-right-radius", [1,1], {valueTemplate:'@@0@@px @@1@@px'} ],
            ["style", "left", '42px'],
            ["style", "height", '171px'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["subproperty", "boxShadow.offsetV", '0px'],
            ["subproperty", "boxShadow.blur", '6px']
         ],
         "${_BOT_RT_SQR}": [
            ["style", "top", '5px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["color", "background-color", 'rgba(0,0,0,1.00)'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '0px'],
            ["subproperty", "boxShadow.blur", '6px']
         ],
         "${_Text2Copy2}": [
            ["style", "top", '239px'],
            ["style", "font-size", '17px'],
            ["style", "left", '299px'],
            ["style", "width", '114px']
         ],
         "${_TOP_RT_ARW}": [
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "left", '1px'],
            ["style", "font-size", '35px'],
            ["style", "top", '0px'],
            ["style", "text-align", 'center'],
            ["subproperty", "filter.drop-shadow.blur", '0px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-weight", 'normal'],
            ["subproperty", "textShadow.color", 'rgba(0,0,0,0.648438)']
         ],
         "${_TextCopy2}": [
            ["style", "top", '214px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "left", '183px']
         ],
         "${_BOT_OPN_RCK_SQR}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "top", '373px'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '285px'],
            ["style", "width", '141px']
         ],
         "${_TextCopy10}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '161px'],
            ["style", "font-size", '35px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-weight", 'normal']
         ],
         "${_TOP_OPN_RCK_BG}": [
            ["style", "top", '67px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '220px'],
            ["style", "width", '141px']
         ],
         "${_BOT_OPN_RCK_TXT}": [
            ["style", "top", '381px'],
            ["style", "font-size", '17px'],
            ["style", "left", '299px'],
            ["style", "width", '114px']
         ],
         "${_TOP_LT_ARW}": [
            ["subproperty", "textShadow.blur", '0px'],
            ["subproperty", "textShadow.offsetH", '0px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "textShadow.offsetV", '0px'],
            ["style", "left", '0px'],
            ["style", "width", '42px'],
            ["style", "top", '0px'],
            ["style", "text-align", 'center'],
            ["style", "font-weight", 'normal'],
            ["style", "font-size", '35px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["subproperty", "textShadow.color", 'rgba(0,0,0,0.648438)']
         ],
         "${_TextCopy3}": [
            ["style", "top", '214px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "left", '27px'],
            ["style", "width", '42px']
         ],
         "${_BOT_LT_SQR}": [
            ["subproperty", "textShadow.blur", '0px'],
            ["subproperty", "textShadow.offsetH", '0px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "textShadow.offsetV", '0px'],
            ["style", "left", '0px'],
            ["style", "width", '42px'],
            ["style", "top", '0px'],
            ["subproperty", "textShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "text-align", 'center'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-size", '35px'],
            ["style", "font-weight", 'normal']
         ],
         "${_SHOES}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "boxShadow.inset", ''],
            ["subproperty", "boxShadow.blur", '6px'],
            ["style", "border-style", 'none'],
            ["style", "border-width", '5px'],
            ["style", "top", '481px'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["subproperty", "boxShadow.offsetV", '0px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "left", '107px']
         ],
         "${_TOP_OPN_RCK_TXT}": [
            ["style", "top", '75px'],
            ["style", "width", '114px'],
            ["style", "left", '234px'],
            ["style", "font-size", '17px']
         ],
         "${_HAT}": [
            ["color", "background-color", 'rgba(255,255,255,1.00)'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["style", "border-style", 'none'],
            ["style", "border-width", '5px'],
            ["style", "top", '13px'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["subproperty", "boxShadow.offsetV", '0px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["style", "left", '107px']
         ],
         "${_TOP_RT}": [
            ["style", "left", '161px']
         ],
         "${_TOP_LT}": [
            ["style", "left", '-6px']
         ],
         "${_TOP_LT_SQR}": [
            ["style", "top", '5px'],
            ["subproperty", "boxShadow.blur", '6px'],
            ["subproperty", "boxShadow.color", 'rgba(0,0,0,0.648438)'],
            ["subproperty", "boxShadow.spread", '0px'],
            ["style", "left", '1px'],
            ["color", "background-color", 'rgba(0,0,0,1.00)']
         ],
         "${_TextCopy13}": [
            ["color", "color", 'rgba(255,210,248,1.00)'],
            ["style", "left", '-5px'],
            ["style", "width", '42px'],
            ["style", "top", '32px'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "font-size", '35px'],
            ["style", "font-weight", 'normal']
         ],
         "${_TOP_IMG}": [
            ["style", "height", '171px'],
            ["style", "top", '0px'],
            ["style", "left", '42px'],
            ["style", "width", '114px']
         ],
         "${_TextCopy4}": [
            ["style", "top", '214px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "left", '183px']
         ],
         "${_Stage}": [
            ["color", "background-color", 'rgba(162,223,255,1.00)'],
            ["style", "width", '960%'],
            ["style", "height", '704px'],
            ["style", "overflow", 'hidden']
         ],
         "${_BOT_LT}": [
            ["style", "left", '-6px']
         ],
         "${_TextCopy6}": [
            ["style", "top", '214px'],
            ["color", "color", 'rgba(255,255,255,1.00)'],
            ["style", "font-family", 'Arial Black, Gadget, sans-serif'],
            ["style", "left", '183px']
         ],
         "${_BOT_RT}": [
            ["style", "left", '161px'],
            ["style", "top", '63px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 0,
         autoPlay: true,
         timeline: [
         ]
      }
   }
}
};


Edge.registerCompositionDefn(compId, symbols, fonts, resources);

/**
 * Adobe Edge DOM Ready Event Handler
 */
$(window).ready(function() {
     Edge.launchComposition(compId);
});
})(jQuery, AdobeEdge, "EDGE-2304136");
