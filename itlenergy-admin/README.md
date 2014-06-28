# APAtSCHE management interface

Web management application for the [APAtSCHE project](https://bitbucket.org/apatscheii/apatsche-web).


## Getting started

This project is built using [Grunt](http://gruntjs.com/), the JavaScript task runner for [node.js](http://nodejs.org/). Download node, then run the following to install Grunt globally:

    npm install -g grunt-cli

Build the project by running:

    grunt

or

    grunt development

The `development` target doesn't minify CSS and JavaScript files.  After building, the `/static/` folder contains the project root.


## API Server

By default, the application connects to the API server at http://localhost:8080/apatsche-web/api/.  To change this value, see client/app.js.

## Changes

See [CHANGELOG.md](CHANGELOG.md).