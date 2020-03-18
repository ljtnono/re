const path = require('path');

const resolve = dir => {
  return path.join(__dirname, dir)
};

// vue-cli 配置项
const BASE_URL = process.env.NODE_ENV === 'production'
  ? '/'
  : '/';

module.exports = {
    publicPath: BASE_URL,
    chainWebpack: config => {
      config.resolve.alias
        .set('@s', resolve('src'))
        .set('@c', resolve('src/components'))
        .set('@a', resolve('src/assets'))
    },
    devServer: {
        port: 8081,
        proxy: {
            '/api':{
                target:'http://127.0.0.1:8080/',
                changeOrigin:true,
                ws:true,
                pathRewrite:{
                    '^/api':''
                }
            }
        }
    }
};
