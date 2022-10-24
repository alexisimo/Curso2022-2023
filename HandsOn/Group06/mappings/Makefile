parser:
	yarrrml-parser -i rules-with-links.yml -o mappings-with-links.ttl
morph:
	python3 -m morph_kgc config.ini
clear:
	clear
all: clear parser morph
