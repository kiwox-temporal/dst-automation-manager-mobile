package net.kiwox.manager.dst.enums;

/**
 * Application parameters.
 * 
 * Available data types: string, integer, long, boolean, float, double
 */
public enum Parameter {

	//
	// String type
	//

	/**
	 * The temporary directory where uploaded files get stored.
	 */
	UPLOAD_TEMP_DIR,
	/**
	 * List of forbidden login usernames (separated by comma)
	 */
	FORBIDDEN_USERNAMES,
	/**
	 * The mail server host, typically an SMTP host.
	 */
	MAIL_SENDER_HOST,
	/**
	 * The username for the account at the mail host.
	 */
	MAIL_SENDER_USERNAME,
	/**
	 * The password for the account at the mail host.
	 */
	MAIL_SENDER_PASSWORD,
	/**
	 * The return email address of the current user.
	 */
	MAIL_SENDER_FROM,
	/**
	 * The path in the mobile device with the file to upload in the test
	 */
	UPLOAD_PATH_MOBILE_FILE,
	/**
	 * The URL to upload the file in the test
	 */
	UPLOAD_MOBILE_URL,
	/**
	 * The path in the mobile device to download the file in the test
	 */
	DOWNLOAD_PATH_MOBILE_FILE,
	/**
	 * The URL to download the file in the test
	 */
	DOWNLOAD_MOBILE_URL,
	/**
	 * Facebook username
	 */
	FACEBOOK_USERNAME,
	/**
	 * Facebook password
	 */
	FACEBOOK_PASSWORD,
	/**
	 * Facebook gallery folder with the image to upload
	 */
	FACEBOOK_FOLDER_NAME,
	/**
	 * Instagram username
	 */
	INSTAGRAM_USERNAME,
	/**
	 * Instagram password
	 */
	INSTAGRAM_PASSWORD,
	/**
	 * Instagram gallery folder with the image to upload
	 */
	INSTAGRAM_FOLDER_NAME,
	/**
	 * Contact to send messages through Whatsapp
	 */
	WHATSAPP_CONTACT,
	/**
	 * Whatsapp gallery folder with the image to upload
	 */
	WHATSAPP_FOLDER_NAME,
	/**
	 * Twitter username
	 */
	TWITTER_USERNAME,
	/**
	 * Twitter password
	 */
	TWITTER_PASSWORD,
	/**
	 * National web URLs to test navigation
	 */
	NATIONAL_WEB_1,
	NATIONAL_WEB_2,
	NATIONAL_WEB_3,
	/**
	 * International web URLs to test navigation
	 */
	INTERNATIONAL_WEB_1,
	INTERNATIONAL_WEB_2,
	INTERNATIONAL_WEB_3,
	/**
	 * Videos to search in Youtube
	 */
	YOUTUBE_VIDEO_1,
	YOUTUBE_VIDEO_2,
	YOUTUBE_VIDEO_3,
	/**
	 * Escalas para el ping test
	 */
	PING_SCALE,
	/**
	 * URL to upload screenshots
	 */
	SCREENSHOT_UPLOAD_URL,
	/**
	 * TikTok username prefix
	 */
	TIKTOK_USERNAME_PREFIX,
	/**
	 * TikTok gallery folder with the video to upload
	 */
	TIKTOK_FOLDER_NAME,
	/**
	 * Address to execute ping
	 */
	PING_ADDRESS,
	/**
	 * Email addresses separated by comma to send test timeout notification.
	 */
	TEST_TIMEOUT_MAIL_TO,
	/**
	 * CC email addresses separated by comma to send test timeout notification.
	 */
	TEST_TIMEOUT_MAIL_CC,
	/**
	 * BCC email addresses separated by comma to send test timeout notification.
	 */
	TEST_TIMEOUT_MAIL_BCC,
	
	//
	// Integer type
	//

	/**
	 * The size for the table data container.
	 */
	TABLE_SIZE,
	/**
	 * The interval (in minutes) between client requests before the servlet container will invalidate this session.
	 */
	MAX_INACTIVE_INTERVAL,
	/**
	 * The mail server port.
	 */
	MAIL_SENDER_PORT,
	/**
	 * Initial delay for Appium task (in probes).
	 */
	APPIUM_TASK_INITIAL_DELAY,
	/**
	 * Execution period for Appium task (in probes).
	 */
	APPIUM_TASK_EXECUTION_PERIOD,
	/**
	 * Timeout in seconds for objects to appear in Appium tests
	 */
	APPIUM_WAIT_TIMEOUT,
	/**
	 * Amount of times to publish in a Twitter test
	 */
	TWITTER_ITERATIONS,
	/**
	 * Initial delay (in seconds) to start screenshot upload task.
	 */
	SCREENSHOT_UPLOAD_TASK_INITIAL_DELAY,
	/**
	 * Period (in seconds) to execute screenshot upload task.
	 */
	SCREENSHOT_UPLOAD_TASK_EXECUTION_PERIOD,
	/**
	 * Max retries to test APN connection before cancelling tests
	 */
	APN_MAX_RETRIES,

	//
	// Long type
	//

	/**
	 * The maximum allowed size (in bytes) before an upload gets rejected.
	 */
	MAX_UPLOAD_SIZE,

	/**
	 * Phone number for users with pre paid line
	 */
	ENTEL_PERU_APP_PRE_PAID_PHONE_NUMBER,

	/**
	 * Phone number for users with post paid line
	 */
	ENTEL_PERU_APP_POST_PAID_PHONE_NUMBER,

	/**
	 * Code of verification for signin app entel peru
	 */
	ENTEL_PERU_APP_VERIFICATION_CODE;


}
