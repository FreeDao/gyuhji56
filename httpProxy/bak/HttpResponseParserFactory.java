package it.geosolutions.httpproxy;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;

public class HttpResponseParserFactory extends DefaultHttpResponseParserFactory{

    @Override
    public HttpMessageParser<HttpResponse> create(
        SessionInputBuffer buffer, MessageConstraints constraints) {
        LineParser lineParser = new BasicLineParser() {

            @Override
            public Header parseHeader(final CharArrayBuffer buffer) {
                try {
                    return super.parseHeader(buffer);
                } catch (ParseException ex) {
                    return new BasicHeader(buffer.toString(), null);
                }
            }

        };
        return new DefaultHttpResponseParser(
            buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

            @Override
            protected boolean reject(final CharArrayBuffer line, int count) {
                // try to ignore all garbage preceding a status line infinitely
                return false;
            }

        };
    }

}
