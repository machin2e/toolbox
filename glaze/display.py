import threading
import sys
import curses


class BasicDisplay():

    class DisplayThread(threading.Thread):

        def __init__(self, inbound_queue):
            self.inbound_queue = inbound_queue
            super(BasicDisplay.DisplayThread, self).__init__()

        def run(self):
            while True:
                msg = self.inbound_queue.get()
                if not msg:
                    break
                print '%s: %s' % (msg['client'], msg['text'])

    def __init__(self, outbound_queue, client_name, inbound_queue):
        self.inbound_queue = inbound_queue
        self.client_name = client_name
        self.outbound_queue = outbound_queue
        self.display_thread = self.setup_display()
        self.display_thread.start()

    def setup_display(self):
        return self.DisplayThread(self.inbound_queue)

    def cleanup_display(self):
        pass    # No-op for basic display

    def queue_text(self, text):
        packet = {'client': self.client_name, 'text': text}
        self.outbound_queue.put(packet)

    def run(self):
        try:
            map(self.queue_text, self.input_lines())
        finally:
            self.cleanup_display()
            self.close()

    def input_lines(self):
        while not sys.stdin.closed:
            line = sys.stdin.readline().strip()
            if line == '':
                raise StopIteration()
            yield line

    def close(self):
        self.inbound_queue.put(False)
        self.outbound_queue.put(False)
        self.display_thread.join()


class CursesDisplay(BasicDisplay):
    KEY_ENTER = 10
    KEY_BACKSPACE = 127

    class DisplayThread(threading.Thread):

        def __init__(self, inbound_queue, chat_win, limit):
            self.inbound_queue = inbound_queue
            self.chat_win = chat_win
            self.limit = limit
            super(CursesDisplay.DisplayThread, self).__init__()

        def run(self):
            messages = []
            while True:
                msg = self.inbound_queue.get()
                if not msg:
                    break
                messages.append(msg)
                # HACK! SUPER INEFFICIENT!
                messages = messages[-self.limit:]
                self.chat_win.erase()
                for i, message in enumerate(messages):
                    self.chat_win.addstr(i, 0, '%s: %s' % (message['client'], message['text']))
                self.chat_win.refresh()

    def setup_display(self):
        limit = self.setup_screen()
        return self.DisplayThread(self.inbound_queue, self.chat_win, limit)

    def setup_screen(self):
        # setup curses / screen config
        self.scr = curses.initscr()
        self.scr.keypad(1)
        curses.cbreak()
        curses.curs_set(False)
        rows, cols = self.scr.getmaxyx()

        # create our windows
        self.div_win = curses.newwin(1, cols + 1, rows - 2, 0) # curses.newwin(1, cols + 1, rows - 3, 0)
        self.chat_win = curses.newwin(rows - 2, cols, 0, 0)
        self.msg_win = curses.newwin(1, cols, rows - 1, 0) # curses.newwin(1, cols, rows - 2, 0)
        # self.instructions_win = curses.newwin(1, cols, rows - 1, 0)

        # set up the divider
        div = '-' * cols
        self.div_win.addstr(0, 0, div)
        self.div_win.refresh()

        # self.instructions_win.addstr(0, 0, 'Type! Hit <ENTER> to send. An empty message quits the program')
        # self.instructions_win.refresh()

        return rows - 3 # rows - 4

    def input_loop(self):
        current_msg = ''
        while True:
            c = self.msg_win.getch()
            if c == CursesDisplay.KEY_ENTER:
                if current_msg == '':
                    break
                else:
                    self.queue_text(current_msg)
                    self.msg_win.erase()
                    self.msg_win.refresh()
                    current_msg = ''
            elif c == CursesDisplay.KEY_BACKSPACE:
                current_msg = current_msg[:-1]
                self.msg_win.erase()
                self.msg_win.refresh()
                self.msg_win.addstr(current_msg) #self.msg_win.delch()
            else:
                # s = chr(c)
                s = chr(c)
                # s = ord(s)
                # s = str(s)
                current_msg += s
                # self.msg_win
                # current_msg += ' '

    def input_lines(self):
        current_msg = ''
        while True:
            c = self.msg_win.getch()
            if c == CursesDisplay.KEY_ENTER:
                if current_msg == 'leave':
                    raise StopIteration
                # elif current_msg == 'erase history':
                #     requests.delete(URL) # Delete all previous posts
                elif current_msg == '':
                    # TODO: "clay: Type something first!"
                    continue
                    # raise StopIteration
                else:
                    self.msg_win.erase()
                    self.msg_win.refresh()
                    line = current_msg
                    current_msg = ''
                    yield line
            elif c == CursesDisplay.KEY_BACKSPACE:
                current_msg = current_msg[:-1]
                self.msg_win.erase()
                self.msg_win.refresh()
                self.msg_win.addstr(current_msg) #self.msg_win.delch()
                #self.msg_win.putp('b')
                # self.msg_win.erasechar()
            else:
                s = chr(c)
                # s = ord(s)
                # s = str(s)
                current_msg += s
                # current_msg += ' '

    def cleanup_display(self):
        self.scr.keypad(0)
        curses.nocbreak()
        curses.curs_set(True)
        curses.endwin()
