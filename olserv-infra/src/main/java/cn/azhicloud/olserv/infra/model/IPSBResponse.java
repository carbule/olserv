package cn.azhicloud.olserv.infra.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/26 09:14
 */
@Data
public class IPSBResponse {

    /**
     * {
     *     "ip": "112.80.123.196",
     *     "country_code": "CN",
     *     "country": "China",
     *     "region_code": "JS",
     *     "region": "Jiangsu",
     *     "city": "Nanjing",
     *     "continent_code": "AS",
     *     "latitude": "32.0589",
     *     "longitude": "118.7738",
     *     "organization": "China Unicom",
     *     "timezone": "Asia/Shanghai"
     * }
     */

    /**
     * Visitor IP address, or IP address specified as parameter
     */
    @JsonProperty("ip")
    private String ip;

    /**
     * Two-letter ISO 3166-1 alpha-2 country code
     */
    @JsonProperty("country_code")
    private String countryCode;

    /**
     * Name of the country
     */
    @JsonProperty("country")
    private String country;

    /**
     * Two-letter ISO-3166-2 state / region code for US and Canada, FIPS 10-4 region codes otherwise
     */
    @JsonProperty("region_code")
    private String regionCode;

    /**
     * Name of the region
     */
    @JsonProperty("region")
    private String region;

    /**
     * Name of the city
     */
    @JsonProperty("city")
    private String city;

    /**
     * Postal code / Zip code
     */
    @JsonProperty("postal_code")
    private String postalCode;

    /**
     * Two-letter continent code
     */
    @JsonProperty("continent_code")
    private String continentCode;

    /**
     * Latitude
     */
    @JsonProperty("latitude")
    private String latitude;

    /**
     * Longitude
     */
    @JsonProperty("longitude")
    private String longitude;

    /**
     * ASN + ISP name
     */
    @JsonProperty("organization")
    private String organization;

    /**
     * Time Zone
     */
    @JsonProperty("timezone")
    private String timezone;
}
