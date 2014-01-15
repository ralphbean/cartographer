/*******************************************************************************
 * Copyright (C) 2014 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.maven.cartographer.preset;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.commonjava.maven.atlas.graph.filter.ProjectRelationshipFilter;
import org.commonjava.maven.atlas.graph.workspace.GraphWorkspace;
import org.commonjava.maven.cartographer.data.CartoDataManager;
import org.commonjava.util.logging.Logger;

@ApplicationScoped
public class PresetSelector
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private Instance<PresetFactory> presetFactoryInstances;

    @Inject
    private CartoDataManager carto;

    private Map<String, PresetFactory> presetFactories;

    protected PresetSelector()
    {
    }

    public PresetSelector( final CartoDataManager carto, final Iterable<PresetFactory> presetFactoryInstances )
    {
        this.carto = carto;
        mapPresets( presetFactoryInstances );
    }

    @PostConstruct
    public void mapPresets()
    {
        mapPresets( presetFactoryInstances );
    }

    private void mapPresets( final Iterable<PresetFactory> presetFilters )
    {
        presetFactories = new HashMap<String, PresetFactory>();
        for ( final PresetFactory filter : presetFilters )
        {
            final String named = filter.getPresetId();
            if ( named != null )
            {
                logger.info( "Loaded preset filter: %s (%s)", named, filter );
                presetFactories.put( named, filter );
            }
            else
            {
                logger.info( "Skipped unnamed preset: %s", filter );
            }
        }
    }

    public ProjectRelationshipFilter getPresetFilter( String preset, final String defaultPreset )
    {
        if ( preset == null )
        {
            preset = defaultPreset;
        }

        if ( preset != null )
        {
            final PresetFactory factory = presetFactories.get( preset );
            if ( factory != null )
            {
                final GraphWorkspace ws = carto.getCurrentWorkspace();
                final ProjectRelationshipFilter filter = factory.newFilter( ws );

                logger.info( "Returning filter: %s for preset: %s", filter, preset );
                return filter;
            }

            // TODO: Is there a more elegant way to handle this?
            throw new IllegalArgumentException( "Invalid preset: " + preset );
        }

        return null;
    }

}