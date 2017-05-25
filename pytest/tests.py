import unittest
import websockettest_stable

class TestMechanic(unittest.TestCase):
    """docstring for Test"""
    def test_error(self):
        websockettest_stable.start(self)

if __name__ == '__main__':
    unittest.main()
