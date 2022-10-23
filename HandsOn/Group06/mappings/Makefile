parser:
	yarrrml-parser -i rules.yml -o mappings.ttl
morph:
	python3 -m morph_kgc config.ini 2> errors.txt
clear:
	rm -f mappings.ttl errors.txt && clear
all: clear parser morph
