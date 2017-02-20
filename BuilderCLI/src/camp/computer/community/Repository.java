package camp.computer.community;

import java.util.UUID;

import camp.computer.OLD_construct.Construct_v1;

public class Repository {

    // TODO: Load repository content from file/Redis!

    // Represents the "public" assets repository.
    // Refactor Concept into generic one and allow making custom types
    // Stream in content for revisions, etc. into same OLD_construct. It's a container for content that changes per-revision.

    public static Construct_v1 clone(UUID uuid) { // add/clone/branch
        // TODO:
        return null;
    }

}
