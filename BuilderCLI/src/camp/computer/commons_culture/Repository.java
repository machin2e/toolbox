package camp.computer.commons_culture;

import java.util.UUID;

import camp.computer.construct.Construct;

public class Repository {

    // TODO: Load repository content from file/Redis!

    // Represents the "public" assets repository.
    // Refactor Construct into generic one and allow making custom types
    // Stream in content for revisions, etc. into same construct. It's a container for content that changes per-revision.

    public static Construct clone(UUID uuid) { // get/clone/branch
        // TODO:
        return null;
    }

}
