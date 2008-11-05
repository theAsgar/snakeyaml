/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import java.nio.charset.Charset;

import org.jvyaml.Mark;

/**
 * @see PyYAML 3.06 for more information
 */
public abstract class Token {
    public final static Token DOCUMENT_START = new DocumentStartToken(null, null);
    public final static Token DOCUMENT_END = new DocumentEndToken(null, null);
    public final static Token BLOCK_MAPPING_START = new BlockMappingStartToken(null, null);
    public final static Token BLOCK_SEQUENCE_START = new BlockSequenceStartToken(null, null);
    public final static Token BLOCK_ENTRY = new BlockEntryToken(null, null);
    public final static Token BLOCK_END = new BlockEndToken(null, null);
    public final static Token FLOW_ENTRY = new FlowEntryToken(null, null);
    public final static Token FLOW_MAPPING_END = new FlowMappingEndToken(null, null);
    public final static Token FLOW_MAPPING_START = new FlowMappingStartToken(null, null);
    public final static Token FLOW_SEQUENCE_END = new FlowSequenceEndToken(null, null);
    public final static Token FLOW_SEQUENCE_START = new FlowSequenceStartToken(null, null);
    public final static Token KEY = new KeyToken(null, null);
    public final static Token VALUE = new ValueToken(null, null);
    public final static Token STREAM_END = new StreamEndToken(null, null);
    public final static Token STREAM_START = new StreamStartToken(null, null, Charset
            .forName("UTF-8"));

    private Mark startMark;
    private Mark endMark;

    public Token(final Mark startMark, final Mark endMark) {
        this.startMark = null;
        this.endMark = null;
    }

    public String toString() {
        return "<" + this.getClass().getName() + "(" + getArguments() + ")>";
    }

    public Mark getStartMark() {
        return startMark;
    }

    public Mark getEndMark() {
        return endMark;
    }

    /**
     * @see __repr__ for Token in PyYAML
     */
    protected String getArguments() {
        return "";
    }

    /**
     * For error reporting.
     * 
     * @see class variable 'id' in PyYAML
     */
    public abstract String getTokenId();

}
