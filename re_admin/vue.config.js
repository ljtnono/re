const path = require('path');

const resolve = dir => {
  return path.join(__dirname, dir)
};

// 设置base_url
const BASE_URL = process.env.NODE_ENV === 'production'
  ? '/admin/'
  : '/';

module.exports = {
  publicPath: BASE_URL,
  lintOnSave: false,
  chainWebpack: config => {
    config.resolve.alias
      .set('@', resolve('src'))
      .set('_c', resolve('src/components'))
  },
  devServer: {
    host: "localhost",
    port: 8888,
    proxy: {
      '/api': {
        target: 'http://192.168.1.8',
        // target: 'http://localhost:8001/',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
};
