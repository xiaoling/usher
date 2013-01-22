package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Fills slots for org:country_of_headquarters using regexes.
 * Needs "tokens", "meta"
 * 
 * @author mwinst
 * 
 */
public class RegexOrgCountryOfHeadquartersFiller2 extends Filler {

	/** Countries of the world. */
	public static final String COUNTRIES =
			"(Afghanistan|Albania|Algeria|Andorra|Angola|Antigua & Deps|" +
			"Argentina|Armenia|Australia|Austria|Azerbaijan|Bahamas|Bahrain|" +
			"Bangladesh|Barbados|Belarus|Belgium|Belize|Benin|Bhutan|Bolivia|" +
			"Bosnia Herzegovina|Botswana|Brazil|Britain|Brunei|Bulgaria|" +
			"Burkina|Burundi|Cambodia|Cameroon|Canada|Cape Verde|" +
			"Central African Republic|Chad|Chile|China|Colombia|Comoros|Congo|" +
			"Democratic Republic of the Congo|Costa Rica|Croatia|Cuba|Cyprus|" +
			"Czech Republic|Denmark|Djibouti|Dominica|Dominican Republic|" +
			"East Timor|Ecuador|Egypt|El Salvador|Equatorial Guinea|Eritrea|" +
			"Estonia|Ethiopia|Fiji|Finland|France|Gabon|Gambia|Georgia|" +
			"Germany|Ghana|Greece|Grenada|Guatemala|Guinea|Guinea-Bissau|" +
			"Guyana|Haiti|Honduras|Hungary|Iceland|India|Indonesia|Iran|Iraq|" +
			"Ireland|Israel|Italy|Ivory Coast|Jamaica|Japan|Jordan|Kazakhstan|" +
			"Kenya|Kiribati|North Korea|South Korea|Kosovo|Kuwait|Kyrgyzstan|" +
			"Laos|Latvia|Lebanon|Lesotho|Liberia|Libya|Liechtenstein|" +
			"Lithuania|Luxembourg|Macedonia|Madagascar|Malawi|Malaysia|" +
			"Maldives|Mali|Malta|Marshall Islands|Mauritania|Mauritius|Mexico|" +
			"Micronesia|Moldova|Monaco|Mongolia|Montenegro|Morocco|Mozambique|" +
			"Myanmar|Burma|Namibia|Nauru|Nepal|Netherlands|New Zealand|" +
			"Nicaragua|Niger|Nigeria|Norway|Oman|Pakistan|Palau|Panama|" +
			"Papua New Guinea|Paraguay|Peru|Philippines|Poland|Portugal|Qatar|" +
			"Romania|Russia|Russian Federation|Rwanda|St Kitts & Nevis|" +
			"St Lucia|Saint Vincent & the Grenadines|Samoa|San Marino|" +
			"Sao Tome & Principe|Saudi Arabia|Senegal|Serbia|Seychelles|" +
			"Sierra Leone|Singapore|Slovakia|Slovenia|Solomon Islands|Somalia|" +
			"South Africa|South Sudan|Spain|Sri Lanka|Sudan|Suriname|Swaziland|" +
			"Sweden|Switzerland|Syria|Taiwan|Tajikistan|Tanzania|Thailand|Togo|" +
			"Tonga|Trinidad & Tobago|Tunisia|Turkey|Turkmenistan|Tuvalu|Uganda|" +
			"Ukraine|UAE|United Arab Emirates|United Kingdom|United States|" +
			"America|U\\.S\\.|Uruguay|Uzbekistan|Vanuatu|Vatican City|" +
			"Venezuela|Vietnam|Yemen|Zambia|Zimbabwe)";
	
	/** U.S. state names and abbreviations for identifying U.S. companies. */
	public static final String US_STATES =
			"(Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|" +
			"Delaware|Florida|Georgia|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|" +
			"Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|Minnesota|" +
			"Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|" +
			"New Jersey|New Mexico|New York|North Carolina|North Dakota|Ohio|" +
			"Oklahoma|Oregon|Pennsylvania|Rhode Island|South Carolina|" +
			"South Dakota|Tennessee|Texas|Utah|Vermont|Virginia|Washington|" +
			"West Virginia|Wisconsin|Wyoming|District of Columbia|D\\.C\\.|" +
			"Puerto Rico|Guam|American Samoa|U\\.S\\. Virgin Islands|" +
			"Virgin Islands|Northern Mariana Islands|" +
			"AL|A\\.L\\.|AK|A\\.K\\.|AZ|A\\.Z\\.|AR|A\\.R\\.|CA|C\\.A\\.|CO|" +
			"C\\.O\\.|CT|C\\.T\\.|DE|D\\.E\\.|FL|F\\.L\\.|GA|G\\.A\\.|HI|" +
			"H\\.I\\.|ID|I\\.D\\.|IL|I\\.L\\.|IN|I\\.N\\.|IA|I\\.A\\.|KS|" +
			"K\\.S\\.|KY|K\\.Y\\.|LA|L\\.A\\.|ME|M\\.E\\.|MD|M\\.D\\.|MA|" +
			"M\\.A\\.|MI|M\\.I\\.|MN|M\\.N\\.|MS|M\\.S\\.|MO|M\\.O\\.|MT|" +
			"M\\.T\\.|NE|N\\.E\\.|NV|N\\.V\\.|NH|N\\.H\\.|NJ|N\\.J\\.|NM|" +
			"N\\.M\\.|NY|N\\.Y\\.|NC|N\\.C\\.|ND|N\\.D\\.|OH|O\\.H\\.|OK|" +
			"O\\.K\\.|OR|O\\.R\\.|PA|P\\.A\\.|RI|R\\.I\\.|SC|S\\.C\\.|SD|" +
			"S\\.D\\.|TN|T\\.N\\.|TX|T\\.X\\.|UT|U\\.T\\.|VT|V\\.T\\.|VA|" +
			"V\\.A\\.|WA|W\\.A\\.|WV|W\\.V\\.|WI|W\\.I\\.|WY|W\\.Y\\.)";
	
	public RegexOrgCountryOfHeadquartersFiller2() {
		slotName = "org:country_of_headquarters";
	}

	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		// the query needs to be a PER type.
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.ORG) {
			return;
		}

		// check if the entity is mentioned.
		String tokens = annotations.get(SFConstants.TOKENS);
		String text = annotations.get(SFConstants.TEXT);
		if (!tokens.contains(mention.mentionString)) {
			return;
		}

		// Get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

		// Match countries to their organizations using surrounding text.
		Pattern[] patternsCountry = {
				Pattern.compile(COUNTRIES + " company " + mention.mentionString),
				Pattern.compile(COUNTRIES + " 's " + mention.mentionString),
				Pattern.compile(mention.mentionString + " of " + COUNTRIES),
				Pattern.compile(COUNTRIES + " - based( company | )" + mention.mentionString),
				Pattern.compile(COUNTRIES + "-based( company | )" + mention.mentionString),

		};
		doMatch(patternsCountry, tokens, false, filename, mention);
		
		// See if this is a U.S. company (of a state) using surrounding text.
		Pattern[] patternsUS = {
				// May contain a city name, so add a regex for the city name.
				Pattern.compile(mention.mentionString + " of( [a-zA-Z\\.]+ , | )" + US_STATES),
				Pattern.compile(mention.mentionString + " in( [a-zA-Z\\.]+ , | )" + US_STATES),
				Pattern.compile(US_STATES + "( - |-)based( company | )" + mention.mentionString),
				Pattern.compile(US_STATES + " 's " + mention.mentionString),
		};
		doMatch(patternsUS, tokens, true, filename, mention);
		
		// See if the name of the organization has the country name in it.
		Pattern[] patternsIntraName = {Pattern.compile(COUNTRIES)};
		doMatch(patternsIntraName, mention.mentionString, false, filename, mention);
		
		// See if the name of the organization has the state name in it (U.S.).
		Pattern[] patternsIntraNameUS = {Pattern.compile(US_STATES)};
		doMatch(patternsIntraNameUS, mention.mentionString, true, filename, mention);
	}
	
	/**
	 * Matches all regex patterns in the given pattern list with the given string.
	 * If a match is found, then the match is saved in the given entity as coming
	 * from the given file and being the matched string (or the U.S. if isUs is
	 * true).
	 * 
	 * @param patterns The regular expressions to test for a match.
	 * @param toUse The string to match on.
	 * @param isUS Whether a match should be construed by default as a U.S. company.
	 * @param filename The filename that the string came from.
	 * @param mention The entity to be slot-filled.
	 */
	private void doMatch(Pattern[] patterns, String toMatch, boolean isUS,
						 String filename, SFEntity mention) {
		for (Pattern p : patterns) {
			Matcher matcher = p.matcher(toMatch);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = isUS ? "U.S." : matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}
}
