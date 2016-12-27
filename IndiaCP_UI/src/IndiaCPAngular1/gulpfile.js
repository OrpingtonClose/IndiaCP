/*
This file in the main entry point for defining Gulp tasks and using Gulp plugins.
Click here to learn more. http://go.microsoft.com/fwlink/?LinkId=518007
*/
var webroot = "./wwwroot/";

var gulp = require("gulp"),
    series = require('stream-series'),
    inject = require('gulp-inject'),
    wiredep = require('wiredep').stream,
    gutil = require('gulp-util');

var paths = {
    ngModule: webroot + "app/**/*.module.js",
    ngRoute: webroot + "app/**/*.routes.js",
    ngController: webroot + "app/**/*.controller.js",
    script: webroot + "scripts/**/*.js",
    style: webroot + "styles/**/*.css"
};

gulp.task('inject:index', function () {
    var moduleSrc = gulp.src(paths.ngModule, { read: false });
    var routeSrc = gulp.src(paths.ngRoute, { read: false });
    var controllerSrc = gulp.src(paths.ngController, { read: false });
    var scriptSrc = gulp.src(paths.script, { read: false });
    var styleSrc = gulp.src(paths.style, { read: false });

    gulp.src(webroot + 'app/index.html')
        .pipe(wiredep())
        .pipe(inject(series(moduleSrc, routeSrc, controllerSrc, scriptSrc), { ignorePath: '/wwwroot' }))
        .pipe(inject(series(styleSrc), {}))
        .pipe(gulp.dest(webroot + 'app'));
});



//gulp.task('default', function () {
//    // place code for your default task here
//});