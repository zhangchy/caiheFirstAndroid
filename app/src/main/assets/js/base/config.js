var dev = 'page';

require.config({
    baseUrl: '/android_asset/js/',
    paths: {
        base: dev ? 'page' : 'dist',
        $: 'base/zepto.min',
        doT: 'base/doT.min'
    },
    map: {
        '*': {
            'css': ''
        }
    },
    shim: {
        $: {
            exports: '$'
        }
    },
    urlArgs: 'v=18'
});