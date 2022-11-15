module.exports = [
    {
        // The different paths
        // Single * match 1 level eg. /api/books but NOT /api/book/123
        // Double * match recursively so both
        context: ['/api/**'],               // match these request and send to springboot
        target: 'http://localhost:8080',     //SpringBoot
        secure: false                       // not using HTTPS
        // ng serve --proxy-config ./proxy-config.js
    }

]
