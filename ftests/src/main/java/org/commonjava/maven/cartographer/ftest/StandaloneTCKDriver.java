package org.commonjava.maven.cartographer.ftest;

import org.commonjava.maven.atlas.graph.spi.neo4j.FileNeo4jConnectionFactory;
import org.commonjava.maven.cartographer.Cartographer;
import org.commonjava.maven.cartographer.CartographerBuilder;
import org.commonjava.maven.cartographer.discover.SourceManagerImpl;
import org.commonjava.maven.galley.cache.FileCacheProvider;
import org.commonjava.maven.galley.event.NoOpFileEventManager;
import org.commonjava.maven.galley.filearc.FileTransport;
import org.commonjava.maven.galley.filearc.ZipJarTransport;
import org.commonjava.maven.galley.io.HashedLocationPathGenerator;
import org.commonjava.maven.galley.io.NoOpTransferDecorator;
import org.commonjava.maven.galley.spi.cache.CacheProvider;
import org.commonjava.maven.galley.spi.event.FileEventManager;
import org.commonjava.maven.galley.spi.io.PathGenerator;
import org.commonjava.maven.galley.spi.io.TransferDecorator;
import org.junit.rules.TemporaryFolder;

public class StandaloneTCKDriver
    implements CartoTCKDriver
{

    private SourceManagerImpl sourceManager;

    private Cartographer carto;

    private FileNeo4jConnectionFactory connectionFactory;

    @Override
    public Cartographer start( final TemporaryFolder temp )
        throws Exception
    {
        sourceManager = new SourceManagerImpl();

        final TransferDecorator decorator = new NoOpTransferDecorator();
        final FileEventManager fileEvents = new NoOpFileEventManager();
        final PathGenerator pathGen = new HashedLocationPathGenerator();

        final CacheProvider cache = new FileCacheProvider( temp.newFolder( "cache" ), pathGen, fileEvents, decorator );

        final FileTransport fileTransport = new FileTransport( temp.newFolder( "pub" ), pathGen );
        final ZipJarTransport zipTransport = new ZipJarTransport();

        connectionFactory = new FileNeo4jConnectionFactory( temp.newFolder( "db" ), false );

        carto = new CartographerBuilder( cache, connectionFactory ).withTransferDecorator( decorator )
                                                                   .withFileEvents( fileEvents )
                                                                   .withTransports( fileTransport, zipTransport )
                                                                   .withSourceManager( sourceManager )
                                                                   .withLocationExpander( sourceManager )
                                                                   .build();

        return carto;
    }

    @Override
    public void stop()
        throws Exception
    {
        if ( connectionFactory != null )
        {
            connectionFactory.close();
        }

        if ( carto != null )
        {
            carto.close();
        }
    }

    @Override
    public void createRepoAlias( final String alias, final String repoResource )
        throws Exception
    {
        System.out.println( "Aliasing 'test' to: " + repoResource );
        sourceManager.withAlias( "test", repoResource );
    }

}