
document.getElementById('inp_file').addEventListener('change', fileChange, false);

function drawImageWithAntiAliasing(context, image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight) {
  if(arguments.length <= 6) {
    dx = sx
    dy = sy
    sx = sy = 0
    
    if(arguments.length <= 4) {
      dWidth = image.width
      dHeight = image.height
    }
    else {
      dWidth = sWidth
      dHeight = sHeight
    }
    
    sWidth = image.width
    sHeight = image.height
  }
  
  var scaleX = dWidth/sWidth
  var scaleY = dHeight/sHeight
  var steps = Math.ceil(Math.log(1/Math.min(scaleX, scaleY))/Math.log(2))
  var stepScaleX = Math.pow(scaleX, 1/steps)
  var stepScaleY = Math.pow(scaleY, 1/steps)
  var imageCanvas = document.createElement('canvas')
  var imageContext = imageCanvas.getContext('2d')
  var stepCanvas = document.createElement('canvas')
  var stepContext = stepCanvas.getContext('2d')

  imageCanvas.width = sWidth
  imageCanvas.height = sHeight
  imageContext.drawImage(image, sx, sy, sWidth, sHeight, 0, 0, sWidth, sHeight)

  for(var i = 0; i < steps; i++) {
    stepCanvas.width = imageCanvas.width * stepScaleX
    stepCanvas.height = imageCanvas.height * stepScaleY
    stepContext.drawImage(imageCanvas, 0, 0, stepCanvas.width, stepCanvas.height)

    imageCanvas.width = stepCanvas.width
    imageCanvas.height = stepCanvas.height
    imageContext.drawImage(stepCanvas, 0, 0, imageCanvas.width, imageCanvas.height)
  }

  context.drawImage(imageCanvas, dx, dy)
}

function fileChange(e) { 
  document.getElementById('file').value = '';

	console.log(e);
     
  var file = e.target.files[0];
 
  if (file.type == "image/jpeg" || file.type == "image/png") {
	   var reader = new FileReader();  
        reader.onload = function(readerEvent) {
   
           var image = new Image();
           image.onload = function(imageEvent) {
              var max_size = 400;
              var w = image.width;
              var h = image.height;
             
              if (w > max_size) { h*=max_size/w; w=max_size; }
             
              var canvas = document.createElement('canvas');
              canvas.width = w;
              canvas.height = h;
              var context = canvas.getContext('2d');
				drawImageWithAntiAliasing(context, image, 0, 0, w, h);
                 
              if (file.type == "image/jpeg") {
                 var dataURL = canvas.toDataURL("image/jpeg", 1.0);
              } else {
                 var dataURL = canvas.toDataURL("image/png");   
              }
	   var inputfile = document.querySelector('.dropimage');
        inputfile.style['background-image'] = 'url('+dataURL+')';
              document.getElementById('file').value = dataURL;
           }
           image.src = readerEvent.target.result;
        }
        reader.readAsDataURL(file);
    // ...
  } else {
    document.getElementById('inp_file').value = ''; 
    alert('Please only select images in JPG- or PNG-format.');  
  }
}


