package eu.artofcoding.massmailer

import groovy.sql.Sql

class MassMailController {

    def dataSource

    /**
     * The mass mail service.
     */
    def massMailService

    def send = {
        // Object params will not be visible in runAsync{}
        def p = params
        // Lookup actionmail_mailing, STATUS = 1
        def sql = new Sql(dataSource)
        def result = sql.rows("SELECT id, email, body FROM actionmail_mailing WHERE aid = ?", [params.aid])
        //println "MassMailController.send: ${result.size()} rows"
        runAsync {
            def mailsSent = 0
            result.each {
                if (it.email) {
                    massMailService.send([
                            host: "mail.local.dom",
                            port: 465,
                            useSSL: true,
                            username: "mailer@example.com", password: "password",
                            charset: "UTF8",
                            to: it.email,
                            from: "sender@example.com", fromName: "Mailer",
                            subject: "Subject",
                            body: it.body
                    ])
                    mailsSent++
                }
            }
            //println "${result.size()}/${mailsSent} mails sent."
            // In der Datenbank speichern, wann und das Actionmail komplett verschickt wurde
            def ts = java.lang.Math.round(new java.util.Date().time / 1000)
            sql.executeUpdate("UPDATE actionmail SET status = 2, sendtimestamp = ? WHERE id = ?", [ts, p.aid])
            sql.commit()
            // Disconnect
            sql.close()
        }
        // Render answer
        render "MASSMAIL JOB ${params.aid} STARTED"
    }

}
