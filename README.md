# SeDeM-EDS-Data-Processor
A project to ease the use of SeDeM Expert Diagram System (SeDeM EDS).
This program aims is to automate the calculation and graphical representations of SeDeM data obtained using experimental methods.

Currently the program's focus is aimed at powders. Future development may see the extension to beads, granules and etc. depending on available time.

**As this is a hobby project, development may be slow and inconsistent.**

For any sugestions, bugs or feedback, please file a issue at the issue tab of the repository.


**Current features:**
- Seperate tabs for every substance tested.
- Every tab shows the:
  - 12 basic SeDeM EDS parameters including:
    - Bulk density
    - Tapped density
    - Inter particle porosity
    - Carr's index
    - Cohesion index
    - Hausner ratio
    - Angle of response
    - Powder flow
    - Loss on drying
    - Hydroscopicity
    - % Particles smaller than 45 micro meters
    - Homogeneity
  - The SeDeM indices:
    - Dimension,
    - Compressibility
    - Flowability
    - Lubricity/Stability
    - Lubricity/Dosage
  - As well as the three final indices:
    - Index Paramater (IP)
    - Index of Profile Paramater (IPP)
    - Index of Good Compressibility (IGC)
  - And finally a SeDeM polygon graph/chart of the substance
- A window that contains a comparison SeDeM polygon chart featuring:
  - A passing line
  - Selectable substances
  - Custom color selection
- Calculation of data from specific inputs.
- Calculation of SeDeM processed values from data.
- Saving and loading of projects as .json files.
- Calculating corrective excipient percentages.
- Saving the calculation of corrective excipients as an image.


**Screenshots:**

![Substance tab](https://github.com/KoosSA/SeDeM-EDS-Data-Processor/blob/master/github_assets/tab_example.png)
![Selective comparison window](https://github.com/KoosSA/SeDeM-EDS-Data-Processor/blob/master/github_assets/comparison.png)
![Corrective excipient window](https://github.com/KoosSA/SeDeM-EDS-Data-Processor/blob/master/github_assets/ce-example.png.png)



