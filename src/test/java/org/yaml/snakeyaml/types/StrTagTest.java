/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * @see http://yaml.org/type/str.html
 */
public class StrTagTest extends AbstractTest {
    private String getData(String data, String key) {
        return (String) getMapValue(data, key);
    }

    public void testString() throws IOException {
        assertEquals("abcd", getData("string: abcd", "string"));
    }

    public void testStringShorthand() throws IOException {
        assertEquals("abcd", getData("string: !!str abcd", "string"));
    }

    public void testStringTag() throws IOException {
        assertEquals("abcd", getData("string: !<tag:yaml.org,2002:str> abcd", "string"));
    }

    public void testUnicode() throws IOException {
        // escaped 8-bit unicode character (u-umlaut):
        assertEquals("\u00fc", load("\"\\xfc\""));
        assertEquals("\\xfc", load("\\xfc"));

        // 2 escaped 8-bit unicode characters (u-umlaut following by e-grave):
        assertEquals("\u00fc\u00e8", load("\"\\xfc\\xe8\""));

        // escaped 16-bit unicode character (em dash):
        assertEquals("\u2014", load("\"\\u2014\""));

        // UTF-32 encoding is explicitly not supported
        // TODO assertEquals("\"\\U2014AAAA\"", load("\"\\U2014AAAA\""));

        // (and I don't have a surrogate pair handy at the moment)
        // raw unicode characters in the stream (em dash)
        assertEquals("\u2014", load("\u2014"));
    }

    /**
     * @see http://code.google.com/p/jvyamlb/issues/detail?id=6
     */
    @SuppressWarnings("unchecked")
    public void testIssueId6() {
        Map<String, String> map = (Map<String, String>) load("---\ntest: |-\n \"Test\r\r (* error here)\"");
        assertEquals("\"Test\n\n(* error here)\"", map.get("test"));
    }

    public void testStrDump() {
        assertEquals("abc\n", dump("abc"));
    }

    public void testUnicodeDump() {
        assertEquals("\\u263a\n", dump("\\u263a"));
        assertEquals("The leading zero must be preserved.", "\\u063a\n", dump("\\u063a"));
    }

    public void testStringIntOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "1");
        String output = dump(map);
        assertTrue(output, output.contains("number: '1'"));
    }

    public void testStringFloatOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "1.1");
        String output = dump(map);
        assertTrue(output, output.contains("number: '1.1'"));
    }

    public void testStringBoolOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "True");
        String output = dump(map);
        assertTrue(output, output.contains("number: 'True'"));
    }

    public void testEmitLongString() throws IOException {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String output = dump(str);
        assertEquals(str + "\n", output);
    }

    public void testEmitLongStringWithCR() throws IOException {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n";
        String output = dump(str);
        assertEquals("'" + str + "\n  '\n", output);
    }

    public void testEmitDoubleQuoted() throws IOException {
        String str = "\"xx\"";
        String output = dump(str);
        assertEquals("'" + str + "'\n", output);
    }

    public void testEmitEndOfLine() throws IOException {
        String str = "xxxxxxx\n";
        String output = dump(str);
        assertEquals("'" + str + "\n  '\n", output);
    }

    public void testDumpUtf16() throws IOException {
        String str = "xxx";
        assertEquals(3, str.toString().length());
        Yaml yaml = new Yaml();
        Charset charset = Charset.forName("UTF-16");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(stream, charset);
        yaml.dump(str, writer);
        assertEquals(str + "\n", stream.toString("UTF-16"));
        assertEquals("Must include BOM: " + stream.toString(), (1 + 3 + 1) * 2, stream.toString()
                .length());
    }
}
