package sf.filler.regex;

import java.util.Map;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta",
 * @author David Mah (mahh)
 *
 */
public class RegexPerCityOfBirthFiller extends Filler {

  public RegexPerCityOfBirthFiller() {
    slotName = "per:city_of_birth";
  }

  @Override
  public void predict(SFEntity mention, Map<String, String> annotations) {
    // the query needs to be a PER type.
    if (mention.ignoredSlots.contains(slotName)
        || mention.entityType != EntityType.PER) {
      return;
    }

    // check if the entity is mentioned.
    String tokens = annotations.get(SFConstants.TOKENS);
    if (!tokens.contains(mention.mentionString)) {
      return;
    }

    // get the filename of this sentence.
    String[] meta = annotations.get(SFConstants.META).split("\t");
    String filename = meta[2];

    String[] names = mention.mentionString.split(" ");
    String first = names[0];
    String last = null;
    if (names.length > 1) {
      last = names[names.length - 1];
    } else {
      last = first;
    }

    ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    // _person was born in _location
    patterns.add(Pattern.compile(mention.mentionString + " was born (in) ([a-zA-Z]+?)\\p{Punct}"));
    patterns.add(Pattern.compile(first + " was born (in) ([a-zA-Z]+?)\\p{Punct}"));
    patterns.add(Pattern.compile(last  + " was born (in) ([a-zA-Z]+?)\\p{Punct}"));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 2);
    }

    // _person was born in _place's _location
    patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile(mention.mentionString + " (.*)was born (.* )in (.+?) ?'s (.+?) "));
    patterns.add(Pattern.compile(first + " (.*)was born (.* )in (.+?) ?'s (.+?) "));
    patterns.add(Pattern.compile(last + " (.*)was born (.* )in (.+?) ?'s (.+?) "));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 4);
    }

    // in _location, where _person was born
    patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile(" in (.+?) , where .*" + mention.mentionString + " was born"));
    patterns.add(Pattern.compile(" in (.+?) , where .*" + first + " was born"));
    patterns.add(Pattern.compile(" in (.+?) , where .*" + last + " was born"));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 1);
    }

    // _person is from _location
    patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile(mention.mentionString + " is from (.+?) "));
    patterns.add(Pattern.compile(first + " (.*)is from (.+?) "));
    patterns.add(Pattern.compile(last + " (.*)is from (.+?) "));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 2);
    }

    // _location-born _person
    patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile("(.+?)-born (.*)" + mention.mentionString));
    patterns.add(Pattern.compile("(.+?)-born (.*)" + first));
    patterns.add(Pattern.compile("(.+?)-born (.*)" + last));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 1);
    }

    // _location native _person
    patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile(" (.+?) native " + mention.mentionString));
    patterns.add(Pattern.compile(" (.+?) native " + first));
    patterns.add(Pattern.compile(" (.+?) native " + last));
    for (Pattern pattern: patterns) {
      attempt_match(pattern, mention, filename, tokens, 1);
    }
  }

  private void attempt_match(Pattern pattern, SFEntity mention, String filename,
                             String tokens, int groupMatchIndex) {
    Matcher matcher = pattern.matcher(tokens);
    if (matcher.find()) {
      SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
      ans.answer = matcher.group(groupMatchIndex).trim();
      ans.doc = filename;
      mention.answers.put(slotName, ans);
    }
  }
}

