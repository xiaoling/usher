      TAC 2010 KBP Evaluation Entity Linking Gold Standard V1.0
                              LDC2010E82

                            December 22, 2010
                      Linguistic Data Consortium


1. Overview

This package contains the 2010 evaluation queries and gold standard KB
link and entity type information for the TAC 2010 KBP Entity Linking
task.  This is a release to NIST only. A subsequent release of this
data to the KBP participants may be made if requested by NIST.

The goal of the Entity Linking task is to determine the correct
matching entity node for each query in TAC 2009/2010 KBP Evaluation
Reference Knowledge Base (LDC2009E58), or return NIL if there is no
matching node. Queries refer to person (PER), organization (ORG), or
geopolitical (GPE) entities.
 
2. Contents

   ./data/

This directory contains data for this package.  There are two files:

  tac_2010_kbp_evaluation_entity_linking_queries.xml

This file contains the evaluation Entity Linking queries. This list
contains 2250 query and node ID sets. There are 749 GPE names (503 with
KB matches, 246 without), 750  ORG names (304 with KB matches, 446 
without), and 751 PER names (213 with KB matches, 538 without).

While preparing the data for this release, annotators were allowed
to make use of web searching to assist in identifying an entity.
Annotators were asked to indicate if they made use of web searching
in making their entity linking judgments.  The table below summarizes
the counts of names in this corpus for which annotators did or did
not use web searching, by entity type:

    Type     Web     No Web
   -------------------------
    GPE      126        623
    ORG      184        567
    PER      156        594

The format of the file is described in kbpentlink.dtd (see below), and
is a slightly modified version of the format used in TAC 2009 KBP 
Evaluation Entity Linking List (LDC2009E64) with the addition of a
field to contain the unique identifier of a node in the TAC 2009 KBP
Evaluation Reference Knowledge Base (LDC2009E58).

Each entry in the query list has a unique ID (query ID), consisting of 
"EL" plus a unique, five-digit zero-padded, integer , e.g., EL00003.
There are gaps in the number sequence, as they are based on an internal 
identifier from the entity linking annotation tool database at LDC.

Each entry in the query list contains an entity element (entity node ID).  
If the entity node ID begins with "E", the text refers to an entity in
the Knowledge Base.

If the entity node ID value begins with "NIL" followed by a sequence
of four digits, the given query is not linked to an entity in the 
Knowledge Base (KB).  Entities with the same "NIL" ID were judged to
refer to the same entity by human annotators.

Knowledge Base links were determined by human annotators, provided
with the query name string and text of the corresponding document, and
searched the Knowledge Base using a specialized search engine. Human
annotators were allowed to use online searching to assist in
determining KB link/NIL status. Queries for which a human annotator
could not confidently determine the KB link status were removed from
the final set.

  tac_2010_kbp_evaluation_entity_linking_query_types.tab

This list is a tab-delimited file with 5 columns. Columns 1 and 2
contain the query ID and entity node ID, respectively, of the entries
in the query list to which each row relates.  Column 3 contains the
entity type (PER, ORG, or GPE) for that entity. Column 4 indicates if
the annotator made use of web searches to make the linking judgment.
Column 5 indicates the the source genre of the document for the query
(WL for web data, or NW for newswire data).

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

For further information about this data release, or the TAC 2010 KBP
project, contact the following project staff at LDC:

Robert Parker, Technical Lead     <parkerrl@ldc.upenn.edu>
Kira Griffitt, Lead Annotator     <kiragrif@ldc.upenn.edu>
Joe Ellis, Lead Annotator	      <joellis@ldc.upenn.edu>
Stephanie Strassel, Consultant    <strassel@ldc.upenn.edu>
Kazuaki Maeda, Consultant         <maeda@ldc.upenn.edu>
--------------------------------------------------------------------------
README created by Robert Parker on December 22, 2010
