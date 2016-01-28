package com.android.vending.billing.utilities;

public class GooglePlayConfig {

	public static final String GOOGLE_MAP_KEY = "AIzaSyAIQRbxd5-StumglMAonEF8ljj7PQbFWjY";

	public static final String PLACE_KEY = "AIzaSyCSvxhy8gtwSFaPmKNGA_deifOi7tC0mks";

	public static final String API_GET_LOCATION_BY_ADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=true&language=en";

	public static final String API_GET_ADDRESS_BY_LOCATION = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true";

	public static final String API_PLACE_NEARBY_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&sensor=true&key=%s";


	/*
	 * When you make an In-app Billing request with this product ID, Google Play
	 * responds as though you successfully purchased an item. The response
	 * includes a JSON string, which contains fake purchase information (for
	 * example, a fake order ID). In some cases, the JSON string is signed and
	 * the response includes the signature so you can test your signature
	 * verification implementation using these responses.
	 */
	public static final String PURCHASE_SUCCESS = "android.test.purchased";

	/*
	 * When you make an In-app Billing request with this product ID Google Play
	 * responds as though the purchase was canceled. This can occur when an
	 * error is encountered in the order process, such as an invalid credit
	 * card, or when you cancel a user's order before it is charged.
	 */
	public static final String PURCHASE_CANCEL = "android.test.canceled";

	/*
	 * When you make an In-app Billing request with this product ID, Google Play
	 * responds as though the purchase was refunded. Refunds cannot be initiated
	 * through Google Play's in-app billing service. Refunds must be initiated
	 * by you (the merchant). After you process a refund request through your
	 * Google Checkout account, a refund message is sent to your application by
	 * Google Play. This occurs only when Google Play gets notification from
	 * Google Checkout that a refund has been made. For more information about
	 * refunds, see Handling IN_APP_NOTIFY messages and In-app Billing Pricing.
	 */
	public static final String PURCHASE_REFUNDED = "android.test.refunded";

	/*
	 * When you make an In-app Billing request with this product ID, Google Play
	 * responds as though the item being purchased was not listed in your
	 * application's product list.
	 */
	public static final String PURCHASE_ITEM_UNVAILABLE = "android.test.item_unavailable";

	/*
	 *	 
	 */
	public static final String SKU = PURCHASE_SUCCESS;
}