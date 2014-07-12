window.onload = openEyes;

var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
	console.log('Connected: ' + frame);
	stompClient.subscribe('/control/start', start);
	stompClient.subscribe('/control/finish', finish);
	stompClient.subscribe('/control/error', error);
 });

fontsize = function () {
	var fontSize = $("#banner").width() * 0.03;
	$("#banner h1").css('font-size', fontSize);
};
$(window).resize(fontsize);
$(document).ready(fontsize);


function start(message) {
	console.log('Start')
	var json = JSON.parse(message.body);
	$("#default-banner").hide();
	$("#custom-banner").html('Writing an app for <em>' + json.user + '</em> ...').show();
	startTyping();
}

function finish(message) {
	console.log('Finish')
	stopTyping();
	setTimeout(function() {
		finishShowDemo(message);
	}, 500);
}

function finishShowDemo(message) {
	var json = JSON.parse(message.body);
	$("#custom-banner").text("All done, I'll tweet you a link!!  Your app looks like this...");
	$("#dragon").hide();
	$("#preview").show();
	$("#preview-image").attr("src", json.url);
	setTimeout(function() {
		reset();
	}, json.runTime);
}

function error(message) {
	console.log('Error')
	reset();
}

function reset() {
	stopTyping();
	$("#default-banner").show();
	$("#custom-banner").hide();
	$("#preview").hide();
	$("#dragon").show();
}

function blink() {
	document.getElementById("body-blink").style.display = 'block';
	setTimeout(openEyes, 200);
}

function openEyes() {
	document.getElementById("body-blink").style.display = 'none';
	setTimeout(blink, 5000 + ((Math.random() - 0.5) * 2000));
}

function startTyping() {
	 $("#back-arm, #front-arm").addClass("animate");
}

function stopTyping() {
	if($("#back-arm").hasClass("animate")) {
		$("#back-arm, #front-arm").one('animationiteration webkitAnimationIteration',
				function() {
					$(this).removeClass("animate");
				});
	}
}


