//set screen width
document.getElementById("width").innerHTML = window.innerWidth;

//detect if touchscreen
var touchElem = document.getElementById("touch");
Modernizr.touch ?  touchElem.innerHTML = "Yes" : touchElem.innerHTML = "No"

//detect if cssanim is supported
var cssanimElem = document.getElementById("cssanim");
Modernizr.cssanimations ?  cssanimElem.innerHTML = "Yes" : cssanimElem.innerHTML = "No";

var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;

var connectionElem =  document.getElementById("connection");
if(connection){
    var connectionTypes = "Unknown Ethernet WiFi 2G 3G 4G None".split(" ");
    connectionElem.innerHTML = connectionTypes[connection.type];
}else{
    connectionElem.innerHTML = "Unknown";
}