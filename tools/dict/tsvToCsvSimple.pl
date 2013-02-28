#/usr/bin/perl
use strict;
use utf8;

my $LOG = 0;

sub logit {
    my $msg = shift;
    print "$msg\n" if ($LOG);
}

while (<>) {
    my $matches = /^([^\t]*)\t(.*)/;
    next if not $matches;

    my ($word, $defLine) = ($1, $2);
    logit "word = $word\n";
    logit "line = $defLine\n";

    $defLine =~ s/\[[^\]]*\]//g;        # strip out [.*]
    $defLine =~ s/\([^\)]*\)//g;        # strip out (.*)
    $defLine =~ s/\<[^\>]*\>//g;        # strip out <.*>
    $defLine =~ s/『.*』//;             # strip out hanzi and brackets
    $defLine =~ s/1 *//;                # strip leading "1 "
    $defLine =~ /([\w][^ㆍ.]*)[ㆍ.]/;   # capture the definition

    my $defn = $1;
    logit "defn = $defn\n";

    print "$word,$defn\n"
}

