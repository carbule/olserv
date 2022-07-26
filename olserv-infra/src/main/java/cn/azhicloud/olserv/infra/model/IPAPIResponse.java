package cn.azhicloud.olserv.infra.model;

import lombok.Data;

/**
 * Response of api: http://ip-api.com/json/{query}
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/25 17:30
 */
@Data
public class IPAPIResponse {

    /**
     * {
     *     "status": "success",
     *     "continent": "亚洲",
     *     "continentCode": "AS",
     *     "country": "中国",
     *     "countryCode": "CN",
     *     "region": "JS",
     *     "regionName": "江苏省",
     *     "city": "Qinnan",
     *     "district": "",
     *     "zip": "",
     *     "lat": 33.1402,
     *     "lon": 119.789,
     *     "timezone": "Asia/Shanghai",
     *     "offset": 28800,
     *     "currency": "CNY",
     *     "isp": "Chinanet",
     *     "org": "Chinanet JS",
     *     "as": "AS4134 CHINANET-BACKBONE",
     *     "asname": "CHINANET-BACKBONE",
     *     "reverse": "",
     *     "mobile": false,
     *     "proxy": false,
     *     "hosting": false,
     *     "query": "121.229.133.28"
     * }
     */

    /**
     * success or fail
     */
    private String status;

    /**
     * included only when status is fail
     * Can be one of the following: private range, reserved range, invalid query
     */
    private String message;

    /**
     * Continent name
     */
    private String continent;

    /**
     * Two-letter continent code
     */
    private String continentCode;

    /**
     * Country name
     */
    private String country;

    /**
     * Two-letter country code ISO 3166-1 alpha-2
     */
    private String countryCode;

    /**
     * Region/state short code (FIPS or ISO)
     */
    private String region;

    /**
     * Region/state
     */
    private String regionName;

    /**
     * City
     */
    private String city;

    /**
     * District (subdivision of city)
     */
    private String district;

    /**
     * Zip code
     */
    private String zip;

    /**
     * Latitude
     */
    private Float lat;

    /**
     * Longitude
     */
    private Float lon;

    /**
     * Timezone (tz)
     */
    private String timezone;

    /**
     * Timezone UTC DST offset in seconds
     */
    private Integer offset;

    /**
     * National currency
     */
    private String currency;

    /**
     * ISP name
     */
    private String isp;

    /**
     * Organization name
     */
    private String org;

    /**
     * AS number and organization, separated by space (RIR).
     * Empty for IP blocks not being announced in BGP tables.
     */
    private String as;

    /**
     * AS name (RIR). Empty for IP blocks not being announced in BGP tables.
     */
    private String asname;

    /**
     * Reverse DNS of the IP (can delay response)
     */
    private String reverse;

    /**
     * Mobile (cellular) connection
     */
    private Boolean mobile;

    /**
     * Proxy, VPN or Tor exit address
     */
    private Boolean proxy;

    /**
     * Hosting, colocated or data center
     */
    private Boolean hosting;

    /**
     * IP used for the query
     */
    private String query;

    public boolean success() {
        return "success".equals(status);
    }
}
