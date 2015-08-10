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
import java.util.UUID

import groovy.transform.AutoClone
import groovy.transform.Canonical
import groovy.transform.ToString

import com.fasterxml.jackson.databind.ObjectMapper
import de.undercouch.bson4jackson.BsonFactory
import org.apache.commons.codec.binary.Base64
import org.bson.BSONEncoder
import org.bson.BSONObject
import org.bson.BasicBSONEncoder
import org.bson.BasicBSONObject

UUID uuid = UUID.randomUUID()

// map with UUID object
byte[] bsonBytes = mapToBsonBytes([TracingID: uuid])
println "BSON bytes: $bsonBytes"
byte[] bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes: $bsonBase64Bytes"
String bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string: $bsonBase64String"
println ''
Map<?, ?> decodedData = decodeBsonBytes(bsonBytes)
println "Decoded data: $decodedData"

println ''

// map with list of the two long values for the UUID
bsonBytes = mapToBsonBytes([TracingID: [uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()]])
println "BSON bytes: $bsonBytes"
bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes: $bsonBase64Bytes"
bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string: $bsonBase64String"
println ''
decodedData = decodeBsonBytes(bsonBytes)
println "Decoded data: $decodedData"
decodedData = [TracingID: new UUID(decodedData.get('TracingID')[0], decodedData.get('TracingID')[1])]
println "Decoded data: $decodedData"

println ''

// map with array of the two long values for the UUID
long[] uuidParts = [uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()].toArray()
bsonBytes = mapToBsonBytes([TracingID: uuidParts])
println "BSON bytes: $bsonBytes"
bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes: $bsonBase64Bytes"
bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string: $bsonBase64String"
println ''
decodedData = decodeBsonBytes(bsonBytes)
println "Decoded data: $decodedData"
decodedData = [TracingID: new UUID(decodedData.get('TracingID')[0], decodedData.get('TracingID')[1])]
println "Decoded data: $decodedData"

println ''

// POJO with UUID object
pojo = new TracingHeader(tracingId: uuid)
ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
ObjectMapper mapper = new ObjectMapper(new BsonFactory())
mapper.writeValue(outputStream, pojo)
bsonBytes = outputStream.toByteArray()

println "BSON bytes: $bsonBytes"
bsonBase64Bytes = Base64.encodeBase64(bsonBytes)
println "BSON Base64 bytes: $bsonBase64Bytes"
bsonBase64String = Base64.encodeBase64String(bsonBytes)
println "BSON Base64 string: $bsonBase64String"
println ''
ByteArrayInputStream inputStream = new ByteArrayInputStream(bsonBytes)
decodedDataPojo = mapper.readValue(inputStream, TracingHeader)
println "Decoded data: $decodedDataPojo"




byte[] mapToBsonBytes(Map<?, ?> values) {
    BSONObject data = new BasicBSONObject()
    data.putAll(values)

    BSONEncoder encoder = new BasicBSONEncoder()
    encoder.encode(data)
}

Map<?, ?> decodeBsonBytes(byte[] bsonBytes) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bsonBytes)
    ObjectMapper mapper = new ObjectMapper(new BsonFactory())
    mapper.readValue(inputStream, Map)
}

@AutoClone
@Canonical
@ToString(cache=true, includeNames=true, includePackage=false)
class TracingHeader {
    UUID tracingId
}




