#!/bin/sh
createdb hibernate_example
createuser -P hibernate_example
psql -U hibernate_example < ../create-tables.sql
