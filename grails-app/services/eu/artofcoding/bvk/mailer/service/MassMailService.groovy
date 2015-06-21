package eu.artofcoding.bvk.mailer.service

import org.apache.commons.mail.SimpleEmail

class MassMailService {

    static transactional = true

    def charset = { email, s, charset, closure ->
        if (!s) return
        //println "charset: $email, $s, $charset"
        if (s.length() > 2) {
            def b = new String(s.getBytes(charset))
            if (b.length() > 0) {
                b.split(",").each {
                    def t = new String(it.getBytes(charset), charset)
                    closure(t, charset)
                }
            }
        }
    }

    /**
     * @param arg A map
     *              host: ""
     *              port: 0
     *              useSSL: true|false
     *              username: "", password: ""
     *              charset: ""
     *              to: "a,b,c"
     *              cc: "a,b,c"
     *              bcc: "a,b,c"
     *              from: "a", fromName
     *              subject: ""
     */
    def send(arg) {
        // Send email
        def email = new SimpleEmail()
        email.with {
            // Host, port, ...
            setHostName(arg.host)
            setSmtpPort(arg.port)
            setAuthentication(arg.username, arg.password)
            if (arg.useSSL || arg.port == 465) {
                setSSL(true)
            }
            // Charset
            if (!arg.charset) {
                arg.charset = "UTF8"
            }
            // To
            charset(email, arg.to, arg.charset, { t, charset -> email.addTo(t, t, charset) })
            // Cc
            charset(email, arg.cc, arg.charset, { t, charset -> email.addCc(t, t, charset) })
            // Bcc
            charset(email, arg.bcc, arg.charset, { t, charset -> email.addBcc(t, t, charset) })
            // Reply-to
            //// TODO setReplyTo()
            // From
            setFrom(arg.from, arg.fromName ?: "Fluxx Mail Service")
            // Subject
            setSubject(arg.subject ?: "Forgotton subject.")
            // Set body
            if (!arg.body) arg.body = "The suprise is - there  i s  n o  surprise."
            // Set charset and body
            setCharset(arg.charset)
            setMsg(new String(arg.body.getBytes(arg.charset), arg.charset))
            // Send it
            addHeader("X-Mailer", "${this.class.simpleName}/0" as String)
            send()
        }
    }

}
