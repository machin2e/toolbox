// Clay object model with REPL

struct Timeline_Manager;
struct Timeline;
struct Event;
struct Action;
struct Message;
struct Message_Queue;
struct Message_Channel;
struct Message_Dispatcher;

typedef unsigned char bool;

typedef struct Timeline_Manager {
  struct Timeline *timeline;
  struct Action *current_action;
} Timeline_Manager;

typedef struct Timeline {
  char uuid[16];
  struct Event *event;
} Timeline;

extern Timeline* Create_Timeline (const char *uuid);

extern bool Delete_Timeline (Timeline *timeline);

extern void Add_Timeline_Event (Timeline *timeline, Event *event, int index);

extern void Remove_Timeline_Event (Timeline *timeline, int index);

extern void Set_Timeline_Event (const Timeline *timeline, const Event *event);

extern Event* Get_Timeline_Event (Timeline *timeline, int index);

typedef struct Event {
  char uuid[16];
  struct Action *action;
  struct Event *previous;
  struct Event *next;
} Event;

extern Event* Create_Event (const char *uuid);

extern void Delete_Event (Event *event);

extern void Set_Event_Behavior (Behavior *behavior);

extern Behavior* Get_Event_Behavior (const Event *event);

typedef struct Action {
  char uuid[16];
  void *script;
  struct Action *previous; // TODO: Needed? Could remove and contain with Event, or use to keep the list of actions in a action tree (the leaves)
  struct Action *next;
} Action;

typedef struct Action_Cache {
  struct Action *front;
} Action_Cache;

typedef struct Message {
  char uuid[16];
  char *prototcol; // "udp", "tcp", "mesh", "http"
	char *source; // "192.168.1.10:4446", "192.168.1.20:80", "<device-uuid>", "http://.../service"
	char *destination;
	char *content;
  struct Message *previous; // i.e., before
  struct Message *next; // i.e., after
} Message;

typedef struct Message_Queue {
  Message *front;
} Message_Queue;

typedef struct Message_Channel {
  char *type;
  Message_Queue *message_queue;
} Message_Channel;

typedef struct Message_Dispatcher {
  Message_Channel message_channels;
} Message_Dispatcher;

extern bool Register_Message_Queue (const Message_Dispatcher *message_dispatcher, const Message_Queue *message_queue);
