//const tailwindcss = require('tailwindcss');
module.exports = {
    plugins: [
      //tailwindcss('./tailwind.config.js'),
        require('postcss-import'),
      // require('tailwindcss'),
        require('autoprefixer'),
        // require('postcss-uncss'),
        require('cssnano') ({
            "preset": "advanced"
        })
    ],
};
