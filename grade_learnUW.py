#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys, argparse
import shutil
import subprocess
import errno
from shell import execute
from timeout import timeout, TimeoutError

def make_student_dir(submission_dir):
    root, dirs, files = os.walk(submission_dir, topdown=True).next()
    for name in files:
        if ' - ' in name:
            toks = name.split(' - ')
            student_name = toks[1].strip()
            file_name = toks[3].strip()
            student_dir = os.path.join(submission_dir, student_name)
            if not os.path.exists(student_dir):
                os.makedirs(student_dir)
            shutil.move(os.path.join(root, name), os.path.join(student_dir, file_name))

def execute_testing_programs(submission_dir):
    src_dir = os.path.dirname(os.path.realpath(__file__))
    cp_files = ['Customer.java', 'CustomerDatabaseTester.java']
    for root, dirs, files in os.walk(submission_dir, topdown=True):
        for student_dir in dirs:
            for f in cp_files:
                fp = os.path.join(src_dir, f)
                shutil.copy(fp, os.path.join(root, student_dir))
            testfiles_cp = "cp -f *.txt '%s'\n" % (os.path.join(root, student_dir))
            execute(testfiles_cp, verbose=True)
            execute_tester_in_student_dir(os.path.join(root, student_dir))

# Timeout the function if it lasts longer than 60 seconds
@timeout(60, os.strerror(errno.ETIMEDOUT))
def execute_tester_in_student_dir(student_dir):
    src_dir = os.path.dirname(os.path.realpath(__file__))
    compile_cmd = 'javac *.java >> grade.out 2>&1'
    run_cmd = 'java CustomerDatabaseTester >> grade.out 2>&1'
    # chdir to student's personal folder
    print 'change dir to %s' % student_dir
    os.chdir(student_dir)
    if os.path.exists('grade.out'):
        os.remove('grade.out')
    try:
        # compile
        execute(compile_cmd, verbose=True)
        # run the testing
        execute("echo '----> General Test' >> grade.out")
        execute(run_cmd, verbose=True)
    except TimeoutError:
        print "TimeoutError: check student\'s program"
        execute("echo 'TimeoutError: timeout when general testing' >> grade.out 2>&1")
    except subprocess.CalledProcessError as detail:
        print "CalledProcessError: %s" % detail

    try: 
        # run the testcases
        execute_testcases()
    finally:
        os.chdir(src_dir)

@timeout(60, os.strerror(errno.ETIMEDOUT))
def execute_testcases():
    testcases = ['java InteractiveDBTester input1.txt < case1.txt | diff - expected_case1.txt',
            'java InteractiveDBTester input1.txt < d1.txt | diff - expected_d1.txt',
            'java InteractiveDBTester input1.txt < r1.txt | diff - expected_r1.txt',
            'java InteractiveDBTester input1.txt < sf1.txt | diff - expected_sf1.txt',
            'java InteractiveDBTester input2.txt < i2.txt | diff - expected_i2.txt',
            'java InteractiveDBTester input3.txt < user3.txt | diff - expected_user3.txt']
    for test_cmd in testcases:
        try:
            execute("echo '\n----> %s' >> grade.out" % test_cmd)
            execute("%s >> grade.out 2>&1" % test_cmd, verbose=True)
        except subprocess.CalledProcessError:
            # ignore
            pass
    return

    

def main():
    parser = argparse.ArgumentParser(description="""
        use this tool to do grading
        """)
    parser.add_argument("submission_dir", help="The submission_dir (the folder downloaded from Learn@UW)")
    parser.add_argument("-s", "--student", help="Re-run for a certain student's directory")
    parser.add_argument("-p", "--path", help="Enter the path of student's directory to run the grading process", action="store_true")
    args = parser.parse_args()
    if args.student is not None:
        execute_tester_in_student_dir(os.path.join(args.submission_dir, args.student))
    elif args.path is not None:
        execute_tester_in_student_dir(args.submission_dir)
    else:
        make_student_dir(args.submission_dir)
        execute_testing_programs(args.submission_dir)
    """
    make_student_dir(args.submission_dir)
    execute_testing_programs(args.submission_dir)
    """
                

if __name__ == '__main__':
    main()
