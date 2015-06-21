class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
            // apply constraints here
            }
        }
        "/massmail/aid/$aid"(controller: "massMail", action: "send")
        "/"(view:"/index")
        "500"(view:'/error')
    }
    
}
