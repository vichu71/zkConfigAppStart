var animationSpeed = 1.0;
function setAnimationSpeed(factor) {
	animationSpeed = factor;
}

/* simple animation functions implemented using the GSAP animation library */
function flyInChildren() {
	new TimelineLite().staggerFrom(this.children, 1.0, {
		opacity: 0.0, y: '+=150', scale: 0.3, 
		rotation: 180, rotationY: 200, delay: 0.1}, 0.1);
}
function zoomIn() {
	TweenLite.to(this, 0.2 / animationSpeed, {ease: Back.easeOut.config(3.0), scale:1.4, zIndex: 100});
}
function zoomOut() {
	TweenLite.to(this, 0.3 / animationSpeed, {scale:1, zIndex: 0});
}
function spinOnce() {
	TweenLite.to(this, 1.0 / animationSpeed, {ease: Back.easeOut.config(3.0), rotation: '+=360'});
}
