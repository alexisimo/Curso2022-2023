parser:
	yarrrml-parser -i rules.yml -o mappings.ttl
morph:
	python3 -m morph_kgc config.ini
clear:
	clear
all: clear parser morph