/**
 * Copyright (C) 2013 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.cartographer.request.build;

import org.commonjava.cartographer.request.GraphDescription;
import org.commonjava.cartographer.request.ProjectGraphRequest;
import org.commonjava.maven.atlas.ident.ref.ProjectRef;
import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;
import org.commonjava.maven.galley.model.Location;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectGraphRequestBuilder<T extends ProjectGraphRequestBuilder<T, R>, R extends ProjectGraphRequest>
    extends SingleGraphRequestBuilder<T, R>
{

    private String projectGavPattern;

    private ProjectVersionRef project;

    public static final class StandaloneProject
        extends ProjectGraphRequestBuilder<StandaloneProject, ProjectGraphRequest>
    {
    }

    public static StandaloneProject newProjectGraphRequestBuilder()
    {
        return new StandaloneProject();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public R build()
    {
        final R recipe = (R) new ProjectGraphRequest();
        configure( recipe );

        return recipe;
    }

    protected void configure( final R recipe )
    {
        recipe.setProjectGavPattern( projectGavPattern );
        super.configure( recipe );
    }

    public T withProjectVersionRef( final ProjectVersionRef ref )
    {
        return withGraph( GraphDescriptionBuilder.newGraphDescriptionBuilder().withRoots( ref ).build() );
    }

    public T withTargetProject( ProjectVersionRef project )
    {
        this.project = project;
        return self;
    }

    public T withProjectGavPattern( String projectGavPattern )
    {
        this.projectGavPattern = projectGavPattern;
        return self;
    }

    @Override
    public T withGraph( GraphDescription graph )
    {
        return super.withGraph( graph );
    }

    @Override
    public T withSource( String source )
    {
        return super.withSource( source );
    }

    @Override
    public T withWorkspaceId( String workspaceId )
    {
        return super.withWorkspaceId( workspaceId );
    }

    @Override
    public T withSourceLocation( Location source )
    {
        return super.withSourceLocation( source );
    }

    @Override
    public T withTimeoutSecs( Integer timeoutSecs )
    {
        return super.withTimeoutSecs( timeoutSecs );
    }

    @Override
    public T withPatcherIds( Collection<String> patcherIds )
    {
        return super.withPatcherIds( patcherIds );
    }

    @Override
    public T withResolve( boolean resolve )
    {
        return super.withResolve( resolve );
    }

    @Override
    public T withInjectedBOMs( List<ProjectVersionRef> injectedBOMs )
    {
        return super.withInjectedBOMs( injectedBOMs );
    }

    @Override
    public T withExcludedSubgraphs( Collection<ProjectVersionRef> excludedSubgraphs )
    {
        return super.withExcludedSubgraphs( excludedSubgraphs );
    }

    @Override
    public T withVersionSelections( Map<ProjectRef, ProjectVersionRef> versionSelections )
    {
        return super.withVersionSelections( versionSelections );
    }
}
