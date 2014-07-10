module.exports = function(grunt) {

  // Add the grunt-mocha-test tasks.
  grunt.loadNpmTasks('grunt-mocha-test');
  grunt.loadNpmTasks('grunt-codo');

  grunt.initConfig({
    // Configure a mochaTest task
    mochaTest: {
      test: {
        options: {
          reporter: 'spec',
          require: 'coffee-script/register'
        },
        src: ['test/**/*.coffee']
      }
    },
    codo: {
      options: {
          // Task-specific options go here.
      },
      your_target: {
          // Target-specific file lists and/or options
      }
    }
  });

  grunt.registerTask('default', 'mochaTest');

};

