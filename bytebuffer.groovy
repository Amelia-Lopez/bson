#!/usr/bin/env groovy

/*
 * Copyright 2015 Mario Lopez Jr
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@Grab('de.undercouch:bson4jackson:2.5.0')
@Grab('org.mongodb:mongo-java-driver:3.0.3')
@Grab('commons-codec:commons-codec:1.10')

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.UUID

import groovy.json.JsonOutput
import groovy.transform.AutoClone
import groovy.transform.Canonical
import groovy.transform.ToString

import com.fasterxml.jackson.databind.ObjectMapper
import de.undercouch.bson4jackson.BsonFactory
import de.undercouch.bson4jackson.BsonGenerator
import org.apache.commons.codec.binary.Base64
import org.bson.BSONEncoder
import org.bson.BSONObject
import org.bson.BasicBSONEncoder
import org.bson.BasicBSONObject

/***
 * Data structures
 **/

UUID uuid1 = UUID.randomUUID()
UUID uuid2 = UUID.randomUUID()
String string1 = 'Panda123'
String string2 = 'cn.shaanxi.qinling'

list = [uuid1, uuid2, string1, string2]
map = [SessionID: uuid1, RequestID: uuid2, User: string1, Domain: string2]

println "UUID 1: $uuid1"
println "UUID 2: $uuid2"
println "String 1: $string1"
println "String 2: $string2"
println ''

/***
 * Encode manually into a byte buffer
 **/
println 'Manually encoded into byte buffer ----------------------------------------------'

final int UUID_SIZE = 16
final String SEPARATOR = '-'
int bufferSize = UUID_SIZE + UUID_SIZE + string1.size() + SEPARATOR.size() + string2.size()

bytes = ByteBuffer.allocate(bufferSize).
	putLong(uuid1.getMostSignificantBits()).
	putLong(uuid1.getLeastSignificantBits()).
	putLong(uuid2.getMostSignificantBits()).
	putLong(uuid2.getLeastSignificantBits()).
	put((string1 + SEPARATOR + string2).getBytes('UTF-8')).
	array()
base64String = Base64.encodeBase64String(bytes)
println "Encoded bytes (${bytes.length}): $bytes"
println "Encoded base64 (${base64String.size()}): $base64String"
println ''

/***
 * Decode manually out from a byte buffer
 **/

byteBuffer = ByteBuffer.wrap(Base64.decodeBase64(base64String))
UUID decodedUUID1 = new UUID(byteBuffer.getLong(), byteBuffer.getLong())
UUID decodedUUID2 = new UUID(byteBuffer.getLong(), byteBuffer.getLong())
byte[] stringBytes = new byte[byteBuffer.remaining()]
byteBuffer.get(stringBytes)
String[] strings = new String(stringBytes, 'UTF-8').split(SEPARATOR)
String decodedString1 = strings[0]
String decodedString2 = strings[1]

println "Decoded UUID 1: $decodedUUID1"
println "Decoded UUID 2: $decodedUUID2"
println "Decoded String 1: $decodedString1"
println "Decoded String 2: $decodedString2"
println ''

/***
 * JSON - list
 **/
println 'JSON list ----------------------------------------------------------------------'
jsonList = JsonOutput.toJson(list)
base64String = Base64.encodeBase64String(jsonList.getBytes('UTF-8'))
println "JSON List (${jsonList.size()}): $jsonList"
println "Encoded base64 JSON list (${base64String.size()}): $base64String"
println ''

/***
 * JSON - map
 **/
println 'JSON map -----------------------------------------------------------------------'
jsonMap = JsonOutput.toJson(map)
base64String = Base64.encodeBase64String(jsonMap.getBytes('UTF-8'))
println "JSON Map (${jsonMap.size()}): $jsonMap"
println "Encoded base64 JSON map (${base64String.size()}): $base64String"
println ''

/***
 * BSON - list
 **/
println 'BSON list ----------------------------------------------------------------------'
BSONObject bsonObject = new BasicBSONObject(['': list])
BSONEncoder bsonEncoder = new BasicBSONEncoder()
byte[] bsonBytes = bsonEncoder.encode(bsonObject)
base64String = Base64.encodeBase64String(bsonBytes)
println "BSON List (${bsonBytes.size()}): $bsonBytes"
println "Encoded base64 BSON list (${base64String.size()}): $base64String"
println ''

/***
 * BSON - map
 **/
println 'BSON map -----------------------------------------------------------------------'
bsonObject = new BasicBSONObject(map)
bsonEncoder = new BasicBSONEncoder()
bsonBytes = bsonEncoder.encode(bsonObject)
base64String = Base64.encodeBase64String(bsonBytes)
println "BSON map (${bsonBytes.size()}): $bsonBytes"
println "Encoded base64 BSON map (${base64String.size()}): $base64String"
println ''

