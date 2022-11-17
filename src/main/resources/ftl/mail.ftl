<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width">
		<meta name="format-detection" content="address=no">
		<meta name="format-detection" content="telephone=no">
		<meta name="format-detection" content="email=no">
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
			width: 685px;
			-premailer-width: 685;
		}
		td.footer {
			background-image: url('https://documents.kiwox.cl/static/mail_footer_gradient.png');
			background-position: center top;
			background-repeat: no-repeat;
			color: #888;
			font-size: 11px;
			line-height: 14px;
			padding: 15px 0 12px 0;
			text-align: center;
			width: 685px;
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

		td.title {
			color: #2b2b2b;
			font-size: 27px;
			font-weight: 300;
			padding: 24px 0 36px 0;
		}
		span.subtitle {
			font-size: .6em;
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
				font-family: 'Helvetica Neue' !important;
				margin: 0 10px !important;
			}

			td.margin {
				width: 30px !important;
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
				font-family: 'Helvetica Neue' !important;
			}
		}

		@media only screen and (-webkit-min-device-pixel-ratio: 2) {
			table.main {
				font-family: 'Helvetica Neue' !important;
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
									<td class="logo"><img src="https://documents.kiwox.cl/static/logo_kiwox_mail.png" alt="Kiwox"></td>
									<td class="margin"></td>
								</tr>
								<tr>
									<td class="margin"></td>
									<td class="title">${title}<br /><span class="subtitle">${subtitle}</span></td>
									<td class="margin"></td>
								</tr>
								<tr>
									<td class="margin"></td>
									<td>${salute}</td>
									<td class="margin"></td>
								</tr>
								<tr>
									<td class="margin"></td>
									<td class="message">${message}</td>
									<td class="margin"></td>
								</tr>
								<tr>
									<td class="margin"></td>
									<td>${preSignature}</td>
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
				<tr>
					<td class="footer">${footer}</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>