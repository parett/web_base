//var msnry = new Masonry( '.grid', {"itemSelector": ".grid-item", "columnWidth": 200 });
var msnry = new Masonry( '.grid', {
	"itemSelector": ".grid-item",
	"isFitWidth": true
});
imagesLoaded(document.querySelector('.grid'), function() { msnry.layout();});
