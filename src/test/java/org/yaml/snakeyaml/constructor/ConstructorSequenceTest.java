package org.yaml.snakeyaml.constructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.scanner.Scanner;
import org.yaml.snakeyaml.scanner.ScannerImpl;

public class ConstructorSequenceTest extends TestCase {

    public void testGetList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList);
    }

    // TODO should it be possible to change default List implementation ?
    public void testGetLinkedList() {
        String data = "!!seq:java.util.LinkedList [ 1, 2, 3 ]";
        List<Object> list = construct(data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof LinkedList);
    }

    public void testDumpList() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(2);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 2]\n", result);
    }

    public void testDumpListSameIntegers() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(1);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 1]\n", result);
    }

    @SuppressWarnings("unchecked")
    private List<Object> construct(String data) {
        Reader reader = new Reader(data);
        Scanner scanner = new ScannerImpl(reader);
        Parser parser = new ParserImpl(scanner);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        IConstructor constructor = new ConstructorImpl(composer);
        List result = (List) constructor.getSingleData();
        return result;
    }
}
