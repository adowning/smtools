wsdluri='http://webserviceserver.appspot.com/EntityAPIService.wsdl'
gensrcdir='./src'
targetpackage=com.google.appengine.codelab.soap.client.gen
genoutdir='./war/WEB-INF/classes'
wsimport -d "$genoutdir" -s "$gensrcdir" -p $targetpackage -keep "$wsdluri"