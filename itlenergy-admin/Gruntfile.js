/*jshint node: true */
"use strict";

module.exports = function(grunt) {

  var modules = [
    'main', 'login', 'apatsche-api', 'messagebox'
  ];

  function moduleFiles(extension, pre) {
    // required to ensure files get included in the right order
    var ret = pre || [];
    for (var i in modules) {
      ret.push('client/'+modules[i]+'/*'+extension);
      ret.push('client/'+modules[i]+'/**/*'+extension);
    }
    return ret;
  }

  // Project configuration.
  grunt.initConfig({

    options: {
      buildTmpDir: 'build',
      outputDir: 'static/build',
      bootstrap: 'bootstrap/bootstrap.less'
    },

    jshint: {
      options: {
        jshintrc: '.jshintrc'
      },
      gruntfile: {
        src: 'Gruntfile.js'
      },
      client: {
        src: ['client/**/*.js']
      }
    },

    less: {
      options: {
        paths: ['bootstrap/']
      },
      development: {
        files: {
          '<%= options.outputDir %>/client.css': ['<%= options.buildTmpDir %>/client.less'],
          '<%= options.outputDir %>/bootstrap.css': ['<%= options.bootstrap %>']
        }
      },
      production: {
        options: {
          yuicompress: true
        },
        files: {
          '<%= options.outputDir %>/client.css': ['<%= options.buildTmpDir %>/client.less'],
          '<%= options.outputDir %>/bootstrap.css': ['<%= options.bootstrap %>']
        }
      }
    },

    uglify: {
      client: {
        files: {
          '<%= options.outputDir %>/client.js': ['<%= options.buildTmpDir %>/client.js'],
          '<%= options.outputDir %>/templates.js': ['<%= options.buildTmpDir %>/templates.js']
        }
      }
    },

    copy: {
      development: {
        files: {
          '<%= options.outputDir %>/client.js': ['<%= options.buildTmpDir %>/client.js'],
          '<%= options.outputDir %>/templates.js': ['<%= options.buildTmpDir %>/templates.js'],
        }
      }
    },

    concat: {
      less: {
        files: {
          '<%= options.buildTmpDir %>/client.less': moduleFiles('.less', ['client/app.less'])
        }
      },
      client: {
        files: {
          '<%= options.buildTmpDir %>/client.js': moduleFiles('.js', ['client/app.js'])
        }
      }
    },

    html2js: {
      options: {
        base: 'client',
        module: 'app-templates',
        rename: function (name) {
          // make views available as "module/view"
          return name.replace(/\.html|templates\//g, '');
        }
      },
      client: {
        src: ['client/**/*.html'],
        dest: '<%= options.buildTmpDir %>/templates.js'
      }
    },

    watch: {
      gruntfile: {
        files: '<%= jshint.gruntfile.src %>',
        tasks: ['jshint:gruntfile']
      },
      clientLess: {
        files: 'client/**/*.less',
        tasks: ['concat:less','less:development']
      },
      clientJs: {
        files: ['client/**/*.js', 'client/**/*.html'],
        tasks: ['jsdev']
      }
    },

    clean: ['<%= options.outputDir %>', '<%= options.buildTmpDir %>']
  });

  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-conventional-changelog');
  grunt.loadNpmTasks('grunt-html2js');

  // Default task.
  grunt.registerTask('default', ['clean', 'jshint', 'html2js', 'concat', 'uglify', 'less:production']);
  grunt.registerTask('development', ['clean', 'jshint', 'html2js', 'concat', 'copy:development', 'less:development']);
  grunt.registerTask('jsdev', ['jshint', 'html2js', 'concat', 'copy:development']);
};
