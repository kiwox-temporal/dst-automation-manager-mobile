<html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width"/>
    <meta name="format-detection" content="address=no"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="format-detection" content="email=no"/>
    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
            -webkit-text-size-adjust: 100%;
        }

        a {
            color: #08c !important;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        a:active {
            text-decoration: underline;
        }

        table.main {
            border: 0;
            border-collapse: collapse;
            border-spacing: 0;
            /*background-color: #08c;*/
            margin: 0 auto;
            padding: 0;
            -premailer-cellpadding: 0;
            -premailer-cellspacing: 0;
            -webkit-font-smoothing: antialiased;
        }

        table.text {
            color: #333;
            /* font-family: 'Lucida Grande', Helvetica, Arial, sans-serif; */
            font-size: 14px;
        }

        td.header {
            height: 16px;
            -premailer-height: 16;
        }

        td.body {
            width: 700px;
            -premailer-width: 700;
        }

        td.footer {
            background-image: url("https://documents.kiwox.cl/static/mail_footer_gradient.png");
            background-position: center top;
            background-repeat: no-repeat;
            color: #888;
            font-size: 11px;
            line-height: 14px;
            padding: 15px 0 12px 0;
            text-align: center;
            width: 800px;
        }

        td.logo {
            text-align: right;
            width: 600px;
        }

        td.logo img {
            max-width: 200px;
        }

        td.margin {
            width: 40px;
        }

        td.margin-footer {
            width: 250px;
        }

        td.title {
            color: #2b2b2b;
            font-size: 27px;
            font-weight: 300;
            padding: 24px 0 36px 0;
        }

        td.title-footer {
            color: #2b2b2b;
            font-size: 27px;
            font-weight: 300;
            padding: 24px 0 36px 0;
            width: 600px;
        }

        span.subtitle-footer {
            font-size: 0.6em;
            font-weight: bold;
        }

        td.message {
            padding: 17px 0 20px 0;
        }

        td.signature {
            padding-bottom: 30px;
        }

        @media only screen and (min-device-width: 320px) and (max-device-width: 568px) {
            body {
                overflow: hidden;
                width: auto !important;
            }

            table.main {
                font-family: "Helvetica Neue" !important;
                margin: 0 10px !important;
            }

            td.margin {
                width: 50px !important;
            }

            td.title {
                width: 250px !important;
                word-break: break-word;
            }

            td.footer {
                background-size: 100%;
            }

            span.copyright {
                display: block;
                padding-top: 15px !important;
            }
        }

        @media only screen and (max-device-width: 768px) {
            table.main {
                font-family: "Helvetica Neue" !important;
            }
        }

        @media only screen and (-webkit-min-device-pixel-ratio: 2) {
            table.main {
                font-family: "Helvetica Neue" !important;
            }
        }
    </style>
</head>
    <body yahoo="fix">
        <table class="main">
            <tbody>
            <tr>
                <td class="header"></td>
            </tr>
            <tr>
                <td class="body">
                    <table class="text">
                        <tbody>
                        <tr>
                            <td class="margin"></td>
                            <td class="logo"></td>
                            <td class="margin"></td>
                        </tr>
                        <tr>
                            <td class="margin"></td>
                            <td class="title">
                                ${title}<br/><span class="subtitle">${subtitle}</span>
                            </td>
                            <td class="margin"></td>
                        </tr>
                        <tr>
                            <td class="margin"></td>
                            <td>${salute} <strong>${requestId}</strong>.</td>
                            <td class="margin"></td>
                        </tr>
                        <tr>
                            <td class="margin"></td>
                            <td class="message">${message}</td>
                            <td class="margin"></td>
                        </tr>
                        <tr>
                            <td class="margin"></td>
                            <td class="message">${preSignature}</td>
                            <td class="margin"></td>
                        </tr>

                        <tr>
                            <td class="margin"></td>
                            <td class="signature">${signature}</td>
                            <td class="margin"></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        <table class="main">
            <tbody>
            <tr>
                <td class="header"></td>
            </tr>
            <tr>
                <td class="body">
                    <table class="text">
                        <tbody>
                        <tr>
                            <td class="margin-footer"></td>
                            <td class="title-footer">
                                <span class="subtitle-footer">${footer}</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        <img
                style="display: block; margin-left: auto; margin-right: auto"
                width="75"
                height="70"
                src="https://documents.kiwox.cl/static/logo_kiwox_mail.png"
                alt="Kiwox"
        />
        <p style="text-align: center; color: #2b2b2b">
            2022 Kiwox Soluciones Tecnol&oacute;gicas&nbsp;
        </p>
        <p style="text-align: center; color: #2b2b2b">
            <a href="http://www.kiwox.net"
            ><span style="color: #808080; text-decoration: underline"
                >www.kiwox.net</span
                ></a
            >
        </p>
        </body>

</html>
