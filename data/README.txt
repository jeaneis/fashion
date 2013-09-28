Webpages scraped from http://www.net-a-porter.com/ (2008/09/08)


info - Extracted info from html (tab delimited)
 From meta-data: meta-keywords, meta-description 
  - these are , separated list for search engines
 From html body: 
  Strings: name, brand, price, color, description
  List of strings (tab separated): categories, keywords (bolded words from description), details, sizeDetails
  List of links: viewMore
  List of images: images 
  List of product ids: recommended, shownWith, otherColors, related
  Measurements:
    measurements.rows
    measurements.header
    measurements.row.i (for each row i fromm 1 to measurements.rows)

images - images for each product 
  (_pp is medium, _sx is small - we only fetch _sx if _pp doesn't exist)
html - raw html for each product
html/sizing - sizing html for each product

