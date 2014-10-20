module.exports = function(grunt) {

  // Add the grunt-mocha-test tasks.
  grunt.loadNpmTasks('grunt-mocha-test');

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
}
  });

  grunt.registerTask('default', 'mochaTest');

};

