#
# Convert the dictionary file into csv that can be imported into 
# the sqlite database.
#
WORD_TYPES = [ "a", "ad", "n", "pron", "vt", "vi", "x" ]

def isWordType(string)
  return WORD_TYPES.index(string.strip) != nil
end


dictFile = ARGV[0]
File.open(dictFile).each_line { |line|
  wordRec = Hash.new()

  # Word
  captures = line.match(/<span.*>(.*)<\/span>/).captures
  wordRec['word'] = captures[0]

  line = line.sub(/<span.*>(.*)<\/span>/, "")

  fields = line.split(",")
  fields.collect!{|f|f.strip}

  # Types (eg. n, ad, etc)
  types = []
  while (field = fields.shift and isWordType(field))
    types.push(field)
  end
  wordRec['type'] = types

  # Korean def'n
  definitions = []
  definitions.push(field)
  definitions.push(fields.collect{|f|f.strip}) if fields.size > 0
  definitions.flatten
  wordRec['definition'] = definitions

#  rescue => exp
#    print "farted here -> "
#    print $!
#    print exp.backtrace.join("\n")
#  end

#  print wordRec.to_s + "\n"

  print wordRec['word'] + ";;" +
        wordRec['definition'].join(",") + ";;" +
#        wordRec.type + ";;" +
        "1;;" +
        "1\n"

}

