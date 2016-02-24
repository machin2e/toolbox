#include <stdio.h>
#include <string.h>

#include "Message_Queue.h"

int main () {
  int i, j;

  printf ("Testing the message queue.\n\n");

  /** Initialize queues */

  Initialize_Message_Queue (&incomingMessageQueue);
  Initialize_Message_Queue (&outgoingMessageQueue);

  printf ("Incoming message queue size: %d\n", incomingMessageQueue.size);
  printf ("Outgoing message queue size: %d\n", outgoingMessageQueue.size);
  printf ("\n");

  printf ("---\n\n");

  Message *message;

  for (j = 0; j < 2; j++) {

    /** Queue incoming messages */

    for (i = 0; i < 8; i++) {

      // Get message at the back of the queue
      message = Get_Next_Message (&incomingMessageQueue);

      if (message != NULL) {
        // Set contents of message
        Initialize_Message (message, "here", "there", "turn lights on");

        Queue_Message (&incomingMessageQueue, message);

        printf ("Q:  ");
        printf ("source: %s, destination: %s, content: %s\n", (*message).source, (*message).destination, (*message).content);

        printf ("Incoming: ");
        printf ("front: %d, ", incomingMessageQueue.front);
        printf ("back: %d, ", incomingMessageQueue.back);
        printf ("size: %d", incomingMessageQueue.size);
        if (incomingMessageQueue.front > incomingMessageQueue.back) {
          printf (" (wrap)");
        }
        printf ("\n");

        printf ("Outgoing: ");
        printf ("front: %d, ", outgoingMessageQueue.front);
        printf ("back: %d, ", outgoingMessageQueue.back);
        printf ("size: %d", outgoingMessageQueue.size);
        if (outgoingMessageQueue.front > outgoingMessageQueue.back) {
          printf (" (wrap)");
        }
        printf ("\n");
      } else {
        printf ("Maximum message count reached.\n");
      }

      printf ("---\n");
    }

    /** Dequeue incoming message */

    for (int i = 0; i < 12; i++) {
      // Get message at the back of the queue
      if (Has_Messages (&incomingMessageQueue)) {
        message = Dequeue_Message (&incomingMessageQueue);

        printf ("D: ");
        printf ("source: %s, destination: %s, content: %s\n", (*message).source, (*message).destination, (*message).content);

        printf ("Incoming: ");
        printf ("front: %d, ", incomingMessageQueue.front);
        printf ("back: %d, ", incomingMessageQueue.back);
        printf ("size: %d", incomingMessageQueue.size);
        if (incomingMessageQueue.front > incomingMessageQueue.back) {
          printf (" (wrap)");
        }
        printf ("\n");

        printf ("Outgoing: ");
        printf ("front: %d, ", outgoingMessageQueue.front);
        printf ("back: %d, ", outgoingMessageQueue.back);
        printf ("size: %d", outgoingMessageQueue.size);
        if (outgoingMessageQueue.front > outgoingMessageQueue.back) {
          printf (" (wrap)");
        }
        printf ("\n");

      } else {
        printf ("There are no messages to dequeue.\n");
      }
      printf ("---\n");
    }
  }
}
