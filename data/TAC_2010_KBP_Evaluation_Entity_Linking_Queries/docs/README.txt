	    TAC 2010 KBP Evaluation Entity Linking Queries
			      LDC2010E36

			    June 11, 2010
		      Linguistic Data Consortium

1. Overview

This package contains the complete set of evaluation queries for the
TAC 2010 KBP Entity Linking task. The goal of the Entity Linking task
is to determine the correct matching entity node for each query in TAC
2009/2010 KBP Evaluation Reference Knowledge Base (LDC2009E58), or return
NIL if there is no matching node. Queries refer to person (PER),
organization (ORG), or geopolitical (GPE) entities.
 
2. Contents

   ./data/

This directory contains data for this package.  There is one file:

  tac_2010_kbp_evaluation_entity_linking_queries.xml

This file contains the Entity Linking Evaluation queries. There are 1500 
queries with document ids from web data, and 750 queries with document ids
from newswire data, broken down evenly between PER, ORG, and GPE entity 
types.

The format of the file is described in kbpentlink.dtd.

Each entry in the query list has a unique ID (query ID), consisting of 
"EL" plus a unique, five-digit zero-padded, integer , e.g., EL00003.
There are gaps in the number sequence, as they are based on an internal 
identifier from the entity linking annotation tool database at LDC.

The Entity Linking queries will be scored based on Knowledge Base
links determined by human annotators. Human annotators were provided
with the query name string and text of the corresponding document, and
searched the Knowledge Base using a specialized search engine. Queries
for which a human annotator could not confidently determine the KB
link status were removed from the final set.

All documents may be found in TAC 2010 KBP Source Data (LDC2010E12).

Please refer to the latest KBP Task Definition, linked from the
Knowledge Base Population section of NIST's TAC website
(http://www.nist.gov/tac/), for further information.

The other files contained in the package are: 

   ./dtd/kbpentlink.dtd - the dtd for the Entity Linking query list
                          xml file

   ./docs/README.txt - this file

3 Copyright Information

(c) 2010 Trustees of the University of Pennsylvania

4 Contact Information

For further information about this data release, or the TAC 2009 KBP
project, contact the following project staff at LDC:

	Heather Simpson, Project Manager     <hsimpson@ldc.upenn.edu>
	Robert Parker, Technical Lead	     <parkerrl@ldc.upenn.edu>
	Stephanie Strassel, Consultant	     <strassel@ldc.upenn.edu>
	Kazuaki Maeda, Consultant	     <maeda@ldc.upenn.edu>
--------------------------------------------------------------------------
README created by Heather Simpson on June 4, 2010
README updated by Robert Parker on June 9, 2010
README updated by Heather Simpson on June 10, 2010
