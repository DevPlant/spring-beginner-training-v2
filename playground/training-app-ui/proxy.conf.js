{  
   "/book/**":{  
      "target":"http://localhost:8080",
      "changeOrigin": true,
      "secure":false
   },
   "/author/**":{  
      "target":"http://localhost:8080",
      "changeOrigin": true,
      "secure":false
   },
   "/account/**":{  
      "target":"http://localhost:8080",
      "changeOrigin": true,
      "secure":false,
      "logLevel": "debug",
      "pathRewrite": {"^/account" : "http://localhost:8080/account"}
   },
   "/login":{  
      "target":"http://localhost:8080",
      "changeOrigin": true,
      "secure":false
   },
   "/logout":{  
      "target":"http://localhost:8080",
      "changeOrigin": true,
      "secure":false
   }
}