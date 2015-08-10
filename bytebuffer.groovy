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

UUID uuid1 = UUID.randomUUID()
UUID uuid2 = UUID.randomUUID()
String string1 = 'Panda Joe'
String string2 = 'cn.shaanxi.qinling'

final int UUID_SIZE = 16
final String SEPARATOR = '-'
int bufferSize = UUID_SIZE + UUID_SIZE + string1.size() + SEPARATOR.size() + string2.size()
println "Buffer size: ${bufferSize}"

bytes = ByteBuffer.allocate(bufferSize).
	putLong(uuid1.getMostSignificantBits()).
	putLong(uuid1.getLeastSignificantBits()).
	putLong(uuid2.getMostSignificantBits()).
	putLong(uuid2.getLeastSignificantBits()).
	put((string1 + SEPARATOR + string2).getBytes('UTF-8')).
	array()

println "Bytes (${bytes.length}): $bytes"




/***
 * Final tests
 **/

// map with UUID object
/*byte[] bsonBytes = mapToBsonBytes([d: uuid])
println "BSON bytes (${bsonBytes.length}): $bsonBytes"
byte[] bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes (${bsonBase64Bytes.length}): $bsonBase64Bytes"
String bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string (${bsonBase64String.size()}): $bsonBase64String"
Map<?, ?> decodedData = decodeBsonBytes(bsonBytes)
println "Decoded data (${decodedData.toString().size()}): $decodedData"

println ''

long hi = uuid.getMostSignificantBits()
long lo = uuid.getLeastSignificantBits()
bsonBytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array()
println "BSON bytes (${bsonBytes.length}): $bsonBytes"
bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes (${bsonBase64Bytes.length}): $bsonBase64Bytes"
bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string (${bsonBase64String.size()}): $bsonBase64String"
byte[] decodedBsonBytes = Base64.decodeBase64(bsonBase64Bytes)
bb = ByteBuffer.wrap(decodedBsonBytes)
long decodedHi = bb.getLong()
long decodedLo = bb.getLong()
UUID decodedUUID = new UUID(decodedHi, decodedLo)  // haven't tested this yet
println "Decoded data (${decodedUUID.toString().size()}): $decodedUUID"
*/