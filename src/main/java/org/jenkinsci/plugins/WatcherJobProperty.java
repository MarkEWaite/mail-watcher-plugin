package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Job;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class WatcherJobProperty extends JobProperty<Job<?, ?>> {

    private final String watcherAddresses;

    @DataBoundConstructor
    public WatcherJobProperty(final String watcherAddresses) {

        this.watcherAddresses = watcherAddresses;
    }

    public String getWatcherAddresses() {

        return watcherAddresses;
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {

            return true;
        }

        @Override
        public JobProperty<?> newInstance(
                final StaplerRequest req,
                final JSONObject formData
        ) throws FormException {

            final JSONObject watcherData = formData.getJSONObject("watcherEnabled");
            if (watcherData.isNullObject()) return null;

            final String addresses = watcherData.getString( "watcherAddresses" );
            if (addresses == null || addresses.isEmpty()) return null;

            return new WatcherJobProperty(addresses);
        }

        public FormValidation doCheckWatcherAddresses(@QueryParameter String value) {

            return MailWatcherMailer.validateMailAddresses(value);
        }

        @Override
        public String getDisplayName() {

            return "Notify when Job configuration changes";
        }
    }
}
